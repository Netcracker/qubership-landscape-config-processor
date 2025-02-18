package org.qubership.landscape.processor.utils;

import org.qubership.landscape.processor.utils.svg.IGraphNodeRenderer;
import org.qubership.landscape.processor.utils.svg.RedCrossRenderer;
import org.qubership.landscape.processor.utils.svg.UnderResearchRenderer;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SVGUtils {
    private static final Pattern REGEXP_VIEW_BOX = Pattern.compile("\\s+viewbox\\s*=\\s*\\\"([^\\\"]+)\\\"", Pattern.CASE_INSENSITIVE);
    public static final IGraphNodeRenderer RED_CROSS_RENDERER = new RedCrossRenderer();
    public static final IGraphNodeRenderer UNDER_RESEARCH_RENDERER = new UnderResearchRenderer();

    /**
     * Reads content of SVG file and adds new layer with red cross above.
     * If no modifications were done - returns null.
     * @param originalFileName  String file name to read content of
     * @return new SVG content with red cross above
     * @throws Exception in case of error
     */
    public static String addExtraGraphLayer(String originalFileName, IGraphNodeRenderer renderer) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(originalFileName)));
        SvgInfo svgInfo = getSvgInfo(content);

        String graphNodeStr = renderer.createGraphNodeString(svgInfo.fromX, svgInfo.fromY, svgInfo.width, svgInfo.height);
        content = content.replace("</svg>", graphNodeStr + "</svg>");

        return content;
    }

    private static final Pattern REG_EXP_SVG = Pattern.compile("<svg\\s+([^>]+)>", Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
    private static final Pattern REG_EXP_WIDTH = Pattern.compile("width\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern REG_EXP_HEIGHT = Pattern.compile("height\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern REG_EXP_VIEWBOX = Pattern.compile("viewbox\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);


    public static SvgInfo getSvgInfo(String svgFileContent) {
        String svgText = readRegExpGroup(svgFileContent, REG_EXP_SVG);
        if (StringUtils.isEmpty(svgText)) return null;

        String widthText  = readRegExpGroup(svgText, REG_EXP_WIDTH);
        String heightText = readRegExpGroup(svgText, REG_EXP_HEIGHT);

        double fromX = 0;
        double fromY = 0;
        double width = 0;
        double height = 0;

        String viewBoxText= readRegExpGroup(svgText, REG_EXP_VIEWBOX);

        if (!StringUtils.isEmpty(viewBoxText)) {
            // values can be delimited by space
            String[] parts = viewBoxText.split(" ");
            if (parts.length == 4) {

                fromX = getDouble(parts[0]);
                fromY = getDouble(parts[1]);
                width = getDouble(parts[2]);
                height = getDouble(parts[3]);

                return new SvgInfo(fromX, fromY, width, height);
            }

            // or values can be delimited by comma
            parts = viewBoxText.split(",");
            if (parts.length == 4) {
                fromX = getDouble(parts[0]);
                fromY = getDouble(parts[1]);
                width = getDouble(parts[2]);
                height = getDouble(parts[3]);

                return new SvgInfo(fromX, fromY, width, height);
            }
        }

        width = getDouble(widthText);
        height = getDouble(heightText);
        if (width != 0 && height != 0) return new SvgInfo(width, height);

        throw new IllegalStateException("Unexpected format of SVG file. Can't parse width/height");
    }

    private static class SvgInfo {
        double fromX = 0;
        double fromY = 0;
        double width;
        double height;

        SvgInfo(double width, double height) {
            this.width = width;
            this.height = height;
        }

        SvgInfo(double fromX, double fromY, double width, double height) {
            this.fromX = fromX;
            this.fromY = fromY;
            this.width = width;
            this.height = height;
        }
    }

    private static String readRegExpGroup(String content, Pattern regExp) {
        Matcher matcher = regExp.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private static double getDouble(String doubleValue) {
        if (doubleValue == null) return 0;
        if (doubleValue.isEmpty()) return 0;
        doubleValue = doubleValue.trim();

        if (doubleValue.endsWith("pt")) doubleValue = doubleValue.substring(0, doubleValue.length() - 2);
        if (doubleValue.endsWith("px")) doubleValue = doubleValue.substring(0, doubleValue.length() - 2);
        if (doubleValue.endsWith("mm")) doubleValue = doubleValue.substring(0, doubleValue.length() - 2);
        if (doubleValue.endsWith("in")) doubleValue = doubleValue.substring(0, doubleValue.length() - 2);
        if (doubleValue.endsWith(",")) doubleValue = doubleValue.substring(0, doubleValue.length() - 1);
        if (doubleValue.endsWith("%")) return 0; // meaningless

        return Double.parseDouble(doubleValue);
    }
}
