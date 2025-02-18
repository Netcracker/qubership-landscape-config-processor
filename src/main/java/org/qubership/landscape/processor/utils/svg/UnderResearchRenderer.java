package org.qubership.landscape.processor.utils.svg;

public class UnderResearchRenderer implements IGraphNodeRenderer {
    @Override
    public String createGraphNodeString(double fromX, double fromY, double width, double height) {
        StringBuilder sb = new StringBuilder();
        sb.append("<g id=\"Layer red cross\">");
        {
            String crossTemplate = "M%f %f l%f %f l%f %f l%f %f l%f %f";
            String crossResult = String.format(crossTemplate,
                    fromX, fromY, 0.0f, height,
                    width, 0.0f, 0.0f, -height,
                    -width, 0.0f);

            double strokeWidth = Math.max(Math.abs(fromX - width), Math.abs(fromY - height)) * 0.03f;

            String styleTemplate = "fill:%s;stroke:%s;stroke-width:%f";
            String styleStr = String.format(styleTemplate, "none", "green", strokeWidth);

            String str = "<path d=\"" + crossResult + "\" style=\"" + styleStr + "\" />";
            sb.append(str);
        }
        sb.append("</g>");

        return sb.toString();
    }
}
