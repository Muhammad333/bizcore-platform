package com.bizcore.dto;

public class ThemeDTO {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String primaryColor;
    private String secondaryColor;
    private String accentColor;
    private String backgroundColor;
    private String textColor;
    private String errorColor;
    private String successColor;
    private String warningColor;
    private String fontFamily;
    private String borderRadius;
    private boolean darkMode;
    private boolean defaultTheme;
    private boolean active;
    private String customCss;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }

    public String getSecondaryColor() { return secondaryColor; }
    public void setSecondaryColor(String secondaryColor) { this.secondaryColor = secondaryColor; }

    public String getAccentColor() { return accentColor; }
    public void setAccentColor(String accentColor) { this.accentColor = accentColor; }

    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

    public String getTextColor() { return textColor; }
    public void setTextColor(String textColor) { this.textColor = textColor; }

    public String getErrorColor() { return errorColor; }
    public void setErrorColor(String errorColor) { this.errorColor = errorColor; }

    public String getSuccessColor() { return successColor; }
    public void setSuccessColor(String successColor) { this.successColor = successColor; }

    public String getWarningColor() { return warningColor; }
    public void setWarningColor(String warningColor) { this.warningColor = warningColor; }

    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }

    public String getBorderRadius() { return borderRadius; }
    public void setBorderRadius(String borderRadius) { this.borderRadius = borderRadius; }

    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }

    public boolean isDefaultTheme() { return defaultTheme; }
    public void setDefaultTheme(boolean defaultTheme) { this.defaultTheme = defaultTheme; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getCustomCss() { return customCss; }
    public void setCustomCss(String customCss) { this.customCss = customCss; }

    public String toCssVariables() {
        return String.format(
            ":root {\n" +
            "  --primary-color: %s;\n" +
            "  --secondary-color: %s;\n" +
            "  --accent-color: %s;\n" +
            "  --background-color: %s;\n" +
            "  --text-color: %s;\n" +
            "  --error-color: %s;\n" +
            "  --success-color: %s;\n" +
            "  --warning-color: %s;\n" +
            "  --font-family: %s;\n" +
            "  --border-radius: %s;\n" +
            "}\n%s",
            primaryColor, secondaryColor, accentColor, backgroundColor,
            textColor, errorColor, successColor, warningColor,
            fontFamily, borderRadius, customCss != null ? customCss : ""
        );
    }
}
