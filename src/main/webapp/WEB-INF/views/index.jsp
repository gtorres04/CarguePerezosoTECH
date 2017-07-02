<%@include file="includes/etiquetas-imports.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Cargue Perezoso</title>
<base href="/">
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
			<a class="navbar-brand" href="#">Cargue Perezoso</a>
		</div>

		<!-- Agrupar los enlaces de navegación, los formularios y cualquier
       otro elemento que se pueda ocultar al minimizar la barra -->
		<div class="collapse navbar-collapse navbar-ex1-collapse">
			<form
				action="<%=CONTEXTO_APPLICATION + ConstantesMappingURL.CARGAR_ARCHIVO%>"
				class="navbar-form navbar-left" method="post"
				enctype="multipart/form-data">
				<div class="form-group">
					<input type="number" class="form-control"
						placeholder="Digite cédula" id="cedula" name="cedula">
				</div>
				<div class="form-group">
					<input class="form-control" type="file" name="archivo" id="archivo">
				</div>
				<button type="submit" class="btn btn-default">Enviar</button>
			</form>
		</div>
	</nav>

	<script src="<c:url value="/frameworks-js/jquery.min.js" />"></script>
	<script src="<c:url value="/frameworks-js/bootstrap.min.js" />"></script>
	<link href="<c:url value="/frameworks-css/bootstrap.min.css" />"
		rel="stylesheet">
	<link href="<c:url value="/frameworks-css/style.css" />"
		rel="stylesheet">
</body>
</html>
