package org.qubership.landscape.processor.model.automerge;

import org.qubership.landscape.processor.utils.TheLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AutoMergeable {
    /**
     * When merging List of AutoMergeable objects it's required to fine equal instances in two lists to perform merge.
     * Equality logic may be different to classic equals() method - so this one must be implemented.
     * @param other AutoMergeable to compare current instance with to equality and allow other instance fields to be merged into current one.
     * @return true in case objects are equals and must be merged
     */
    protected abstract boolean allowMergeFrom(AutoMergeable other);

    public void mergeValuesFrom(AutoMergeable other) {
        if (!this.getClass().equals(other.getClass()))
            throw new IllegalStateException("Attempt to merge different classes. Current = " + this.getClass() + ", other = " + other.getClass());

        try {
            mergeFromThrowException(other);
        } catch (IllegalAccessException ex) {
            TheLogger.error("Error while merging other = " + other + " values into this = " + this, ex);
            throw new IllegalStateException(ex);
        }
    }

    private void mergeFromThrowException(AutoMergeable other) throws IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation anIgnore = field.getAnnotation(IgnoreWhenAutoMerging.class);
            if (anIgnore != null) continue;

            // if simple field is found
            String className = field.getType().toString();
            field.setAccessible(true);

            if (className.startsWith("class java.lang.")) {
                Object otherValue = field.get(other);

                // check if field is marked with @Concatenate annotation
                Annotation anConcat = field.getAnnotation(ConcatenateWhenMerge.class);
                if (anConcat == null) {
                    field.set(this, otherValue);
                } else {
                    Object thisValue = field.get(this);
                    String thisStrValue = (thisValue == null ? "" : thisValue + " ");
                    String thatStrValue = otherValue == null ? "" : otherValue.toString();
                    field.set(this, thisStrValue  + thatStrValue);
                }
                continue;
            }

            if (className.startsWith("interface java.util.List")) {
                List<Object> otherList = (List<Object>) field.get(other);
                List<Object> thisList = (List<Object>) field.get(this);

                // if List of Automergeable
                if (isAutoMergeable(field)) {
                    if (otherList == null) continue;

                    for (Object otherObj : otherList) {
                        AutoMergeable otherAMObj = (AutoMergeable) otherObj;

                        boolean isMerged = false;
                        for (Object thisObj : thisList) {
                            AutoMergeable thisAMObj = (AutoMergeable) thisObj;

                            if (thisAMObj.allowMergeFrom(otherAMObj)) {
                                thisAMObj.mergeValuesFrom(otherAMObj);
                                isMerged = true;
                                break;
                            }
                        }

                        if (!isMerged) {
                            thisList.add(otherObj);
                        }
                    }

                } else {
                    if (otherList != null) {
                        for (Object otherObj : otherList) {
                            if (!thisList.contains(otherObj)) {
                                thisList.add(otherObj);
                            }
                        }
                    }
                }
                continue;
            }

            Object thisValue = field.get(this);
            Object thatValue = field.get(other);
            if (thisValue instanceof AutoMergeable && thatValue instanceof AutoMergeable) {
                ((AutoMergeable) thisValue).mergeValuesFrom((AutoMergeable) thatValue);
            }

        }
    }

    private static boolean isAutoMergeable(Field field) {

        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            type = pType.getActualTypeArguments()[0];
        } else {
            type = field.getType();
        }

        return AutoMergeable.class.isAssignableFrom((Class<?>) type);
    }
}
