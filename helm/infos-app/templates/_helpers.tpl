{{- define "infos-app.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "infos-app.fullname" -}}
{{- printf "%s-%s" .Release.Name (include "infos-app.name" .) | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "infos-app.labels" -}}
app.kubernetes.io/name: {{ include "infos-app.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}
