# Infinito SMTP Service - Notas de Instalación y Uso

Este servicio proporciona una API REST para el envío de correos electrónicos a través de un servidor SMTP configurado.

## Requisitos
- Java 11 o superior.
- Maven 3.6+.
- Servidor SMTP (Gmail, Outlook, Amazon SES, etc.).

## Configuración
Edite el archivo `src/main/resources/application.properties` o proporcione las variables de entorno necesarias:

```properties
spring.mail.host=smtp.ejemplo.com
spring.mail.port=587
spring.mail.username=tu-usuario
spring.mail.password=tu-contraseña
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
app.mail.from=no-reply@infinito.com
```

## Endpoints Principales

### 1. Enviar Correo HTML
**POST** `/api/v1/mail/send`

**JSON:**
```json
{
  "to": "cliente@correo.com",
  "subject": "Bienvenida",
  "message": "<h1>Hola</h1><p>Este es un mensaje de prueba.</p>"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/mail/send \
     -H "Content-Type: application/json" \
     -d '{
  "to": "cliente@correo.com",
  "subject": "Bienvenida",
  "message": "<h1>Hola</h1><p>Este es un mensaje de prueba.</p>"
}'
```

### 2. Enviar Correo con Archivo Adjunto
**POST** `/api/v1/mail/send-file`

Este endpoint permite enviar un archivo ya existente (PDF, Excel, Imagen, etc.) como adjunto. Utiliza `multipart/form-data`.

**Parámetros (Form-Data):**
- `to`: Correo del destinatario (Obligatorio)
- `subject`: Asunto del correo (Opcional)
- `message`: Contenido del mensaje, puede ser HTML (Obligatorio)
- `file`: El archivo binario a adjuntar (Obligatorio)

**cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/mail/send-file \
     -F "to=destinatario@correo.com" \
     -F "subject=Envío de Archivo" \
     -F "message=Hola, te adjunto el archivo solicitado." \
     -F "file=@/ruta/a/tu/archivo.xlsx"
```

## Notas Adicionales
- La carpeta `.idea/` ha sido excluida del repositorio para evitar conflictos de configuración del IDE.
- Las respuestas de la API utilizan el código de estado `202 Accepted` para indicar que la solicitud de envío ha sido procesada.
