#!/usr/bin/env bash
# =====================================================================
# run_audit.sh
# Ejecuta TODA la auditoria del Taller Docker (Reto 2 y Reto 3) de una sola
# vez para que el estudiante obtenga el "Registro crudo de terminal".
#
# Uso recomendado (Linux/macOS/WSL2):
#   export STUDENT_ID="TU_ID_AQUI"
#   script audit_session.log -c "bash run_audit.sh"
# o simplemente:
#   STUDENT_ID="TU_ID_AQUI" bash run_audit.sh | tee audit_session.log
#
# Requisitos: Docker Engine activo.
# =====================================================================
set -euo pipefail

STUDENT_ID="${STUDENT_ID:-1001234567}"
IMAGE="eco-air-backend:1.0.0"

echo "############################################################"
echo "# TALLER DOCKER - AUDITORIA FORENSE - eco-air-backend"
echo "# STUDENT_ID = ${STUDENT_ID}"
echo "# Fecha inicio: $(date -u '+%Y-%m-%dT%H:%M:%SZ')"
echo "############################################################"

# Limpieza previa de contenedores de pruebas anteriores.
echo
echo "### Limpiando contenedores previos..."
docker rm -f eco_air_active eco_air_crash eco_air_calib_256 eco_air_calib_384 eco_air_calib_512 2>/dev/null || true

# ---------------- RETO 1: Build -------------------------------------------------
echo
echo "=================================================="
echo "RETO 1: Construccion multietapa con huella estricta"
echo "=================================================="
docker build --build-arg STUDENT_ID="${STUDENT_ID}" -t "${IMAGE}" .

echo
echo "--- Tabla de Identidad: Hash de la imagen ---"
docker inspect --format='{{.Id}}' "${IMAGE}"

# ---------------- RETO 2: Auditoria forense -------------------------------------
echo
echo "=================================================="
echo "RETO 2: Auditoria forense y comprobacion de vulnerabilidades"
echo "=================================================="

echo
echo "### 2.1 Verificacion del desacoplamiento de capas"
docker history --human --format "table {{.Size}}\t{{.CreatedBy}}" "${IMAGE}"

echo
echo "### 2.2 Inspeccion del proceso del host (aislamiento de privilegios)"
docker run -d --name eco_air_active -p 8080:8080 "${IMAGE}"
sleep 5
docker top eco_air_active -o pid,user,comm

echo
echo "### 2.3 Test de penetracion: inmutabilidad de runtime"
docker exec eco_air_active touch /etc/crontab_fake 2>&1 || true

echo
echo "### 2.4 Auditoria de artefactos de desarrollo"
echo "--- which mvn ---"
docker exec eco_air_active which mvn 2>&1 || true
echo "--- find / -name '*.java' ---"
docker exec eco_air_active find / -name "*.java" 2>/dev/null || true

echo
echo "### Tabla de Identidad: variable OPERATOR_ID"
docker exec eco_air_active env | grep OPERATOR_ID

# ---------------- RETO 3: cgroups y OOM ----------------------------------------
echo
echo "=================================================="
echo "RETO 3: Calibracion de cgroups y captura del asesino OOM"
echo "=================================================="

echo
echo "### 3.1 Induccion del colapso por memoria (48m -> OOMKilled)"
docker run -d --name eco_air_crash -m 48m "${IMAGE}"
echo "Esperando 10 segundos..."
sleep 10
docker inspect eco_air_crash --format='Status: {{.State.Status}} | ExitCode: {{.State.ExitCode}} | OOMKilled: {{.State.OOMKilled}}'

echo
echo "### 3.2 Calibracion del limite operativo minimo"
# Detenemos la instancia activa anterior para reutilizar el nombre.
docker rm -f eco_air_active 2>/dev/null || true

for MEM in 256m 384m 512m; do
  NAME="eco_air_calib_${MEM//m/}"
  echo "--- Probando limite: ${MEM} ---"
  docker rm -f "${NAME}" 2>/dev/null || true
  docker run -d --name "${NAME}" -m "${MEM}" -p 8080:8080 "${IMAGE}" >/dev/null
  sleep 12
  echo "Logs (Started EcoAirApplication):"
  docker logs --timestamps "${NAME}" 2>&1 | grep "Started EcoAirApplication" || echo "  (no aparecio el mensaje de arranque)"
  echo "docker stats:"
  docker stats --no-stream "${NAME}"
done

echo
echo "### Tabla de Identidad: consumo final calibrado (instancia activa recomendada)"
docker stats --no-stream eco_air_active 2>/dev/null || docker stats --no-stream eco_air_calib_512

echo
echo "### Limpieza final..."
docker rm -f eco_air_active eco_air_crash eco_air_calib_256 eco_air_calib_384 eco_air_calib_512 2>/dev/null || true

echo
echo "############################################################"
echo "# AUDITORIA FINALIZADA: $(date -u '+%Y-%m-%dT%H:%M:%SZ')"
echo "############################################################"
