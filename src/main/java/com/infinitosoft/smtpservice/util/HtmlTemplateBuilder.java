package com.infinitosoft.smtpservice.util;

public final class HtmlTemplateBuilder {

    private HtmlTemplateBuilder() {}

    public static String buildEmail(String title, String contentHtml) {
        String safeTitle = (title == null || title.isBlank()) ? "Notificación" : title;
        String body = (contentHtml == null) ? "" : contentHtml;
        return "<!doctype html>" +
                "<html lang=\"es\">" +
                "<head>" +
                "  <meta charset=\"UTF-8\"/>" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>" +
                "  <title>" + escapeHtml(safeTitle) + "</title>" +
                "  <style>" +
                "    body{font-family:Arial,Helvetica,sans-serif;background:#f6f9fc;margin:0;padding:0;color:#202124;}" +
                "    .container{max-width:640px;margin:24px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 6px 24px rgba(0,0,0,0.08);}" +
                "    .header{background:#0d6efd;color:#fff;padding:16px 24px;font-size:18px;font-weight:700;}" +
                "    .content{padding:24px;line-height:1.6;font-size:15px;}" +
                "    .footer{padding:16px 24px;font-size:12px;color:#5f6368;background:#fafafa;border-top:1px solid #eee;}" +
                "    .btn{display:inline-block;padding:10px 16px;border-radius:8px;background:#0d6efd;color:#fff;text-decoration:none;margin-top:12px;}" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class=\"container\">" +
                "    <div class=\"header\">" + escapeHtml(safeTitle) + "</div>" +
                "    <div class=\"content\">" + body + "</div>" +
                "    <div class=\"footer\">Este mensaje fue enviado por Infinito SMTP Service.</div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
