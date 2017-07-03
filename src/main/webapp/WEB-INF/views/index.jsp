<%@include file="includes/etiquetas-imports.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title><fmt:message key="title.tituloprincipal" bundle="${lang}" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="includes/contexto.jsp"%>
</head>
<body>
	<nav class="barra-formulario navbar navbar-default" role="navigation">
		<!-- El logotipo y el icono que despliega el menú se agrupan
       para mostrarlos mejor en los dispositivos móviles -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-ex1-collapse">
				<span class="sr-only">Desplegar navegación</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#"><fmt:message
					key="title.tituloprincipal" bundle="${lang}" /></a>
		</div>

		<!-- Agrupar los enlaces de navegación, los formularios y cualquier
       otro elemento que se pueda ocultar al minimizar la barra -->
		<div class="collapse navbar-collapse navbar-ex1-collapse">
			<form
				action="<%=CONTEXTO_APPLICATION + ConstantesMappingURL.CARGAR_ARCHIVO_URL_MAPPING%>"
				class="navbar-form navbar-left" method="post"
				enctype="multipart/form-data">
				<div class="form-group">
					<input type="number" class="form-control"
						placeholder="<fmt:message key="placeholder.input.cedula" bundle="${lang}" />"
						id="cedula" name="cedula" required="required">
				</div>
				<div class="form-group">
					<input class="form-control" type="file" name="archivo" id="archivo"
						required="required" accept=".txt">
				</div>
				<button type="submit" class="btn btn-default">
					<fmt:message key="boton.enviar.input" bundle="${lang}" />
				</button>
			</form>
		</div>
	</nav>

	<c:choose>
		<c:when test="${!empty error}">
			<div class="alerta alert alert-danger alert-dismissable">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				${error}
			</div>
		</c:when>
	</c:choose>

	<c:choose>
		<c:when test="${!empty trazaIntento}">
			<div class="panel-respuesta panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">
						<fmt:message key="title.panel.titulo_resultado" bundle="${lang}" />
					</h3>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-1">
							<b><fmt:message key="label.cedula" bundle="${lang}" /></b>
						</div>
						<div class="col-md-1">${trazaIntento.cedula}</div>
						<div class="col-md-1">
							<b><fmt:message key="label.fecha" bundle="${lang}" /></b>
						</div>
						<div class="col-md-3">${trazaIntento.fechaEjecucion}</div>
						<div class="col-md-1">
							<b><fmt:message key="label.entrada" bundle="${lang}" /></b>
						</div>
						<div class="col-md-2">
							<a
								href="<%=CONTEXTO_APPLICATION + ConstantesMappingURL.DESCARGAR_ARCHIVO_OUTPUT_URL_MAPPING%>${trazaIntento.archivoInput.nombreTemporal}"><span
								class="glyphicon glyphicon-cloud-download"></span> <fmt:message
									key="label.descarga" bundle="${lang}" /></a>
						</div>
						<div class="col-md-1">
							<b><fmt:message key="label.salida" bundle="${lang}" /></b>
						</div>
						<div class="col-md-2">
							<a
								href="<%=CONTEXTO_APPLICATION + ConstantesMappingURL.DESCARGAR_ARCHIVO_OUTPUT_URL_MAPPING%>${trazaIntento.archivoOutput.nombreTemporal}"><span
								class="glyphicon glyphicon-cloud-download"></span> <fmt:message
									key="label.descarga" bundle="${lang}" /></a>
						</div>
					</div>
				</div>
			</div>
		</c:when>
	</c:choose>
	<c:choose>
		<c:when test="${!empty historialTrazaIntento}">
			<div class="panel-historial panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">
						<fmt:message key="title.panel.titulo_historial" bundle="${lang}" />
					</h3>
				</div>
				<div class="panel-body">
					<div class="row">
						<div class="mayuscula col-md-1">
							<b><fmt:message key="label.cedula" bundle="${lang}" /></b>
						</div>
						<div class="mayuscula col-md-3">
							<b><fmt:message key="label.fecha" bundle="${lang}" /></b>
						</div>
						<div class="mayuscula col-md-2">
							<b><fmt:message key="label.entrada" bundle="${lang}" /></b>
						</div>
						<div class="mayuscula col-md-2">
							<b><fmt:message key="label.salida" bundle="${lang}" /></b>
						</div>
					</div>
					<c:forEach items="${historialTrazaIntento}" var="trazaIntento">
						<div class="row">
							<div class="col-md-1">${trazaIntento.cedula}</div>
							<div class="col-md-3">${trazaIntento.fechaEjecucion}</div>
							<div class="col-md-2">
								<a
									href="<%=CONTEXTO_APPLICATION + ConstantesMappingURL.DESCARGAR_ARCHIVO_OUTPUT_URL_MAPPING%>${trazaIntento.archivoInput.nombreTemporal}"><span
									class="glyphicon glyphicon-cloud-download"></span> <fmt:message
										key="label.descarga" bundle="${lang}" /></a>
							</div>
							<div class="col-md-2">
								<a
									href="<%=CONTEXTO_APPLICATION + ConstantesMappingURL.DESCARGAR_ARCHIVO_OUTPUT_URL_MAPPING%>${trazaIntento.archivoOutput.nombreTemporal}"><span
									class="glyphicon glyphicon-cloud-download"></span> <fmt:message
										key="label.descarga" bundle="${lang}" /></a>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</c:when>
	</c:choose>
	<script src="<c:url value="/frameworks-js/jquery.min.js" />"></script>
	<script src="<c:url value="/frameworks-js/bootstrap.min.js" />"></script>
	<link href="<c:url value="/frameworks-css/bootstrap.min.css" />"
		rel="stylesheet">
	<link href="<c:url value="/frameworks-css/style.css" />"
		rel="stylesheet">
</body>
</html>
