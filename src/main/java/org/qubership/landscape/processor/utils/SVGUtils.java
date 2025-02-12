package org.qubership.landscape.processor.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SVGUtils {
    private static final Pattern REGEXP_VIEW_BOX = Pattern.compile("\\s+viewbox\\s*=\\s*\\\"([^\\\"]+)\\\"", Pattern.CASE_INSENSITIVE);

    /**
     * Reads content of SVG file and adds new layer with red cross above.
     * If no modifications were done - returns null.
     * @param originalFileName  String file name to read content of
     * @return new SVG content with red cross above
     * @throws Exception in case of error
     */
    public static String createSvgWithCrossStrokes(String originalFileName) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(originalFileName)));

        // try find viewbox attribute - to get image sizes
        Matcher matcher = REGEXP_VIEW_BOX.matcher(content);
        if (matcher.find()) {
            String viewBoxString = matcher.group(1);

            // ensure view box consists of 4 parts (fromX, fromY, toX, toY)
            String[] parts = viewBoxString.split("\\s");
            if (parts.length != 4) {
                throw new IllegalStateException("Unexpected number of part in the view-box string. Current value is " + parts.length);
            }

            float fromX = Float.parseFloat(parts[0]);
            float fromY = Float.parseFloat(parts[1]);
            float toX   = Float.parseFloat(parts[2]);
            float toY   = Float.parseFloat(parts[3]);

            StringBuilder sb = new StringBuilder();
            sb.append("<g id=\"Layer red cross\">");
            {
                String crossTemplate = "M%f %f L%f %f M%f %f L%f %f";
                String crossResult = String.format(crossTemplate,
                        fromX, fromY, toX, toY,
                        fromX, toY, toX, fromY);

                float strokeWidth = Math.max(Math.abs(fromX - toX), Math.abs(fromY - toY)) * 0.03f;

                String styleTemplate = "fill:%s;stroke:%s;stroke-width:%f";
                String styleStr = String.format(styleTemplate, "none", "red", strokeWidth);

                String str = "<path d=\"" + crossResult + "\" style=\"" + styleStr + "\" />";
                sb.append(str);
            }
            sb.append("</g>");


            content = content.replace("</svg>", sb + "</svg>");

        } else {
            return null;
        }

        return content;
    }
}
