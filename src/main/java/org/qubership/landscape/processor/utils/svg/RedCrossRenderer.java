package org.qubership.landscape.processor.utils.svg;

public class RedCrossRenderer implements IGraphNodeRenderer {
    @Override
    public String createGraphNodeString(double fromX, double fromY, double width, double height) {
        StringBuilder sb = new StringBuilder();
        sb.append("<g id=\"Layer red cross\">");
        {
            String crossTemplate = "M%f %f l%f %f m%f %f l%f %f";
            String crossResult = String.format(crossTemplate,
                    fromX, fromY, width, height,
                    -width, 0.0f, width, -height);

            double strokeWidth = Math.max(Math.abs(fromX - width), Math.abs(fromY - height)) * 0.03f;

            String styleTemplate = "fill:%s;stroke:%s;stroke-width:%f";
            String styleStr = String.format(styleTemplate, "none", "red", strokeWidth);

            String str = "<path d=\"" + crossResult + "\" style=\"" + styleStr + "\" />";
            sb.append(str);
        }
        sb.append("</g>");

        return sb.toString();
    }
}
