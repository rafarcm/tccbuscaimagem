<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Busca Imagens</title>
	<script src="http://www.google.com/jsapi" type="text/javascript"></script>
	<script src="js/scripts_image.js" type="text/javascript"></script>
	<script src="js/prototype.js" type="text/javascript"></script>
</head>

<body style="font-family: Arial; border: 0 none;">
	<table width="100%">
		<tr>
			<td align="center">
				<table>
					<tr>
						<td><input type="text" id="entrada" size="80"/></td>
						<td><input type="button" value="Pesquisar" onclick="clearTableResult();slideshowOnLoad()" /></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr id="filtro_pesquisa">
			<td>
				<div id="filtro_conteudo" style="display: none">
					<table width="100%">
						<tr>
							<td colspan="4" align="center">
								<span style="font-size: 14px"><b>Filtro Para Pesquisa por Conteúdo<b/>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center"><span style="font-size: 13px"></span></td>
						</tr>
						<tr>
							<td width="25%">
								<span style="font-size: 13px"><b>Características da imagem:<b/></span>
							</td>
							<td width="25%">
								<span style="font-size: 13px">Cor</span>
								<input type="checkbox" name="caracteristica" onclick="javascript:mostrarDescritores(this,'_cor');"/>
							</td>
							<td width="25%">
								<span style="font-size: 13px">Forma</span>
								<input type="checkbox" name="caracteristica" onclick="javascript:mostrarDescritores(this,'_forma');"/>
							</td>
							<td width="25%">
								<span style="font-size: 13px">Textura</span>
								<input type="checkbox" name="caracteristica" onclick="javascript:mostrarDescritores(this,'_textura');"/>
							</td>
						</tr>
						<tr>
							<td width="25%" valign="top">
								<span id="desc_extrator" style="display: none; font-size: 13px"><b>Extratores:<b/></span>
							</td>
							<td width="25%"  valign="top">
								<div id="extrator_cor" style="display: none">
									<table border="0">
										<tr>
											<td  valign="top">
												<img src="imagem/botao_help.jpg" alt="Help" title="Relação de cores mais fracas da imagem."/>
												<span style="font-size: 13px">Cores Fracas</span>
											</td>
											<td  valign="top"> 
												<input type="checkbox" name="extratores_cor" value="1"/>
											</td>
										</tr>
										<tr>
											<td  valign="top">
												<img src="imagem/botao_help.jpg" alt="Help" title="Relação de cores médias da imagem."/>
												<span style="font-size: 13px">Cores Médias</span>
											</td>
											<td  valign="top"> 
												<input type="checkbox" name="extratores_cor" value="2"/>
											</td>
										</tr>
										<tr>
											<td  valign="top">
												<img src="imagem/botao_help.jpg" alt="Help" title="Relação de cores mais fortes da imagem."/>
												<span style="font-size: 13px">Cores Fortes</span>
											</td>
											<td  valign="top"> 
												<input type="checkbox" name="extratores_cor" value="3"/>
											</td>
										</tr>
									</table>
								</div>
							</td>
							<td width="25%"  valign="top">
								<div id="extrator_forma" style="display: none">
									<table border="0">
										<tr>
											<td  valign="top">
												<img src="imagem/botao_help.jpg" alt="Help" title="Razão entre a altura e a largura dos objetos dentro da imagem."/>
												<span style="font-size: 13px">Razão Altura/Largura</span>
											</td>
											<td  valign="top">
												<input type="checkbox" name="extratores_forma" value="5"/>
											</td>
										</tr>
										<tr>
											<td  valign="top">
												<img src="imagem/botao_help.jpg" alt="Help" title="Número de regiões distintas contidas na imagem."/>
												<span style="font-size: 13px">Área-Ponto</span>
											</td>
											<td  valign="top">
												<input type="checkbox" name="extratores_forma" value="4"/>
											</td>
										</tr>
										<tr>
											<td  valign="top">
												<img src="imagem/botao_help.jpg" alt="Help" title="Representa a fronteira da imagem."/>
												<span style="font-size: 13px">Assinaturas</span>
											</td>
											<td  valign="top">
												<input type="checkbox" name="extratores_forma" value="6"/>
											</td>
										</tr>
									</table>
								</div>
							</td>
							<td width="25%"  valign="top">
								<div id="extrator_textura" style="display: none">
									<table border="0">
										<tr>
											<td  valign="top">
												<img src="imagem/botao_help.jpg" alt="Help" title="Indica a direção mais importante da textura a ser examinada."/>
												<span style="font-size: 13px">Probabilidade Máxima</span>
											</td>
											<td  valign="top">
												<input type="checkbox" name="extratores_textura" value="11"/>
											</td>
										</tr>
										<tr>
											<td  valign="top">
												<img src="imagem/botao_help.jpg" alt="Help" title="Medida da diferença de brilho entre as áreas claras e escuras da imagem."/>
												<span style="font-size: 13px">Contraste</span>
											</td>
											<td  valign="top">
												<input type="checkbox" name="extratores_textura" value="7"/>
											</td>
										</tr>
										<tr>
											<td  valign="top">
												<img src="imagem/botao_help.jpg" alt="Help" title="Medida da diferença de brilho entre as áreas claras e escuras da imagem de forma invertida."/>
												<span style="font-size: 13px">Inverso do Contraste</span>
											</td>
											<td  valign="top">
												<input type="checkbox" name="extratores_textura" value="8"/>
											</td>
										</tr>
										<tr>
											<td  valign="top">
												<img src="imagem/botao_help.jpg" alt="Help" title="Mede o grau de desordem entre os pixels da imagem."/>
												<span style="font-size: 13px">Entropia</span>
											</td>
											<td  valign="top">
												<input type="checkbox" name="extratores_textura" value="9"/>
											</td>
										</tr>
										<tr>
											<td  valign="top">
												<img src="imagem/botao_help.jpg" alt="Help" title="Mede o grau de uniformidade dos pixels da imagem."/>
												<span style="font-size: 13px">Uniformidade</span>
											</td>
											<td  valign="top">
												<input type="checkbox" name="extratores_textura" value="10"/>
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td width="25%">
								<span style="font-size: 13px"><b>Número de resultados:<b/></span>
							</td>
							<td width="75%" colspan="3">
								<select id="num_resultados">
									<option value="0"></option>
									<option value="10">10</option>
									<option value="20">20</option>
									<option value="30">30</option>
									<option value="40">40</option>
									<option value="50">50</option>
									<option value="60">60</option>
								</select>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td align="center">
				<table border="0">
					<tr>
						<td align="center" width="25%">
							<table id="table_result"></table>
						</td>
						<td align="center" width="75%">
							<iframe name="similares_frame" id="similares_frame" frameborder="0" width="100%" height="100%" ></iframe>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
<form action="buscarImagens" id="imagensForm" method="post" target="similares_frame">
</form>
</body>
</html>