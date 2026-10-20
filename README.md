# Taller Docker — eco-air-backend

Taller de la materia POO: **Contenerización, Seguridad de Runtime y Estrés de cgroups**.

- **Estudiante:** Hector Alejandro Riascos Insuasty
- **Código (STUDENT_ID):** `871752`

El backend `eco-air-backend` procesa alertas ambientales críticas de una estación
meteorológica. El reto consiste en encapsularlo en una imagen de producción **ligera**,
**segura** (non-root) y con **cuotas estrictas de memoria** (cgroups / OOM).

## Estructura de archivos

```
taller-docker-semana13/
├── .dockerignore           # Contexto de build mínimo
├── .gitignore              # Ignora artefactos de build e intermedios
├── .gitattributes          # Normaliza finales de línea (LF) en el repo
├── Dockerfile              # Build multietapa (Reto 1)
├── pom.xml                 # Proyecto Spring Boot 3.2 + Java 17 + H2
├── src/                    # Código del backend (alertas ambientales)
├── run_audit.sh            # Ejecuta toda la auditoría de una vez
├── audit_session.log       # Registro crudo de terminal (generado con run_audit.sh)
├── FORENSIC_REPORT.md      # Reporte forense con evidencias
└── FORENSIC_REPORT.pdf     # Reporte forense en PDF (entregable)
```

## Requisitos

- Docker Engine activo (Linux, macOS o WSL2).
- No se necesita Maven ni Java instalados localmente: la compilación ocurre dentro de la
  imagen `maven:3.9.6-eclipse-temurin-17-alpine`.

## Construir la imagen (Reto 1)

```bash
docker build --build-arg STUDENT_ID="871752" -t eco-air-backend:1.0.0 .
```

## Generar el registro de auditoría (audit_session.log)

```bash
export STUDENT_ID="871752"
bash run_audit.sh | tee audit_session.log
# o, para un log crudo tipo `script`:
script audit_session.log -c "STUDENT_ID=871752 bash run_audit.sh"
```

## Reporte forense

Las evidencias (hash de la imagen, `OPERATOR_ID`, `docker top`, OOM, calibración de
cgroups) están documentadas en `FORENSIC_REPORT.md` y exportadas en `FORENSIC_REPORT.pdf`.
