package com.infinitosoft.smtpservice.util;

public final class HtmlTemplateBuilder {

    private HtmlTemplateBuilder() {}

    public static String buildEmail(String title, String contentHtml) {
        String safeTitle = (title == null || title.isBlank()) ? "Notificación" : title;
        String body = (contentHtml == null) ? "" : contentHtml;
        String logoImgTag = "<img src=\"cid:logo\" width=\"64\" height=\"64\" alt=\"Logo\" style=\"display:block;border:0;\"/>";

        return "<!doctype html>" +
                "<html lang=\"es\">" +
                "<head>" +
                "  <meta charset=\"UTF-8\"/>" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>" +
                "  <title>" + escapeHtml(safeTitle) + "</title>" +
                "  <style>" +
                "    body{font-family:Arial,Helvetica,sans-serif;background:#f6f9fc;margin:0;padding:0;color:#202124;}" +
                "    .container{max-width:640px;margin:24px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 6px 24px rgba(0,0,0,0.08);}" +
                "    .header{background:#1e1b4b;color:#ffffff;padding:20px 24px;}" +
                "    .content{padding:24px;line-height:1.6;font-size:15px;}" +
                "    .footer{padding:16px 24px;font-size:12px;color:#5f6368;background:#fafafa;border-top:1px solid #eee;}" +
                "  </style>" +
                "</head>" +
                "<body>" +
                "  <div class=\"container\">" +
                "    <div class=\"header\">" +
                "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                "        <tr>" +
                "          <td width=\"64\" style=\"padding-right:15px;vertical-align:middle;\">" + logoImgTag + "</td>" +
                "          <td style=\"vertical-align:middle;font-size:20px;font-weight:bold;color:#ffffff;\">" + escapeHtml(safeTitle) + "</td>" +
                "        </tr>" +
                "      </table>" +
                "    </div>" +
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
