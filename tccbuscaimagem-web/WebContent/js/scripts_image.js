google.load("search", "1");
var indice;
var indiceUrl=0;

/**
 * Limpa a tabela de resultados.
 */
function clearTableResult() {
	document.getElementById('filtro_conteudo').style.display = "block";
	document.getElementById('table_result').innerHTML = '';
	document.getElementById('imagensForm').innerHTML = '';
	
	if(document.getElementById('similares_frame').contentWindow) {
		document.getElementById('similares_frame').contentWindow.document.body.innerHTML = '';
	} else if(document.getElementById('similares_frame').contentDocument){
		document.getElementById('similares_frame').contentDocument.document.body.innerHTML = '';
	}
	indiceUrl = 0;
}

/**
 * Executa uma chamada a API do Google para recuperar as imagens.
 */
function slideshowOnLoad() {
	indice = 1;
	var imageSearch = new google.search.ImageSearch;
	imageSearch.setSearchCompleteCallback(this, createSearchTable,
			[ imageSearch ]);
	imageSearch.setResultSetSize(google.search.Search.LARGE_RESULTSET);

	//Aplica o filtro para as imagens que serão retornadas.
	//Só vai retornar imagens jpg.
	imageSearch.setRestriction(google.search.ImageSearch.RESTRICT_FILETYPE,
			google.search.ImageSearch.FILETYPE_JPG);
	
	imageSearch.setRestriction(google.search.ImageSearch.RESTRICT_IMAGETYPE,
			google.search.ImageSearch.IMAGETYPE_PHOTO);

	//Realiza a busca
	imageSearch.execute(document.getElementById('entrada').value);
}

/**
 * Cria a tabela com as imagens, retornadas pela API do Google, dinâmicamente.
 */
function createSearchTable(searcher) {
	if (searcher.results && searcher.results.length > 0) {
		var contCell = 0;

		var tableResult = document.getElementById('table_result');

		for ( var i = 0; i < searcher.results.length; i++) {

			var rowImage;
			var cellImage;

			var rowButton;
			var cellButton;
			if (contCell == 0) {
				rowImage = tableResult.insertRow(-1);
				cellImage = rowImage.insertCell(0);
				cellImage.setAttribute("align", "center");

				rowButton = tableResult.insertRow(-1);
				cellButton = rowButton.insertCell(0);
				cellButton.setAttribute("align", "center");

				contCell = 1;
			} else {
				rowImage = tableResult.rows[tableResult.rows.length - 2];
				cellImage = rowImage.insertCell(1);
				cellImage.setAttribute("align", "center");

				rowButton = tableResult.rows[tableResult.rows.length - 1];
				cellButton = rowButton.insertCell(1);
				cellButton.setAttribute("align", "center");

				contCell = 0;
			}
			//Cria a imagem para exibição
			var imageTag = document.createElement('img');

			imageTag.setAttribute("id", 'image-container');
			imageTag.setAttribute("src", searcher.results[i].tbUrl);

			cellImage.appendChild(imageTag);

			//Cria os campos hidden para subir a URL para o servidor
			//URL da imagem original
			var hiddenUrl = document.createElement('input');

			hiddenUrl.setAttribute("name", "urlsForm.urls");
			hiddenUrl.setAttribute("type", "hidden");
			hiddenUrl.setAttribute("value", searcher.results[i].url);

			//URL da imagem em miniatura
			var hiddenMinUrl = document.createElement('input');

			hiddenMinUrl.setAttribute("name", "urlsForm.minUrls");
			hiddenMinUrl.setAttribute("type", "hidden");
			hiddenMinUrl.setAttribute("value", searcher.results[i].tbUrl);
			indiceUrl++;

			//Adiciona os novos hidden's ao form
			document.getElementById('imagensForm').appendChild(hiddenUrl);
			document.getElementById('imagensForm').appendChild(hiddenMinUrl);

			cellButton.innerHTML = '<button type="button" onclick="enviarUrl(\''+searcher.results[i].tbUrl+'\');">Pesquisar</button>';
		}

		nextPage(searcher);
	}
}

/**
 *Recupera a próxima página na paginação.
 */
function nextPage(searcher) {
	searcher.gotoPage(indice++);
}

google.setOnLoadCallback(slideshowOnLoad);

function enviarUrl(urlPesquisa) { 
	if(validarForm()){
		if(document.getElementById('urlsForm.numeroResultados')){
			document.getElementById('imagensForm').removeChild(document.getElementById('urlsForm.numeroResultados'));
		}
		
		if(document.getElementById('urlsForm.imagemPesquisa')){
			document.getElementById('imagensForm').removeChild(document.getElementById('urlsForm.imagemPesquisa'));
		}
		
		if(document.getElementsByName('urlsForm.extratores')){
			var tamanho = document.getElementsByName('urlsForm.extratores').length;
			for (var i = 0; i < tamanho; i++) { 
				if(document.getElementById('urlsForm.extratores['+i+']')){
					document.getElementById('imagensForm').removeChild(document.getElementById('urlsForm.extratores['+i+']'));
				}
			}
		}
		
		var pesquisa = document.createElement('input');
		pesquisa.setAttribute("id", "urlsForm.imagemPesquisa");
		pesquisa.setAttribute("name", "urlsForm.imagemPesquisa");
		pesquisa.setAttribute("type", "hidden");
		pesquisa.setAttribute("value", urlPesquisa);
	
		document.getElementById('imagensForm').appendChild(pesquisa);
		
		var numeroResultados = document.createElement('input');
		numeroResultados.setAttribute("id", "urlsForm.numeroResultados");
		numeroResultados.setAttribute("name", "urlsForm.numeroResultados");
		numeroResultados.setAttribute("type", "hidden");
		numeroResultados.setAttribute("value", document.getElementById("num_resultados").value);
	
		document.getElementById('imagensForm').appendChild(numeroResultados);
		
		var j = 0;
		var caracteristicas = document.getElementsByName("extratores_cor");
		for (var i = 0; i < caracteristicas.length; i++) { 
		    if (caracteristicas[i].checked) { 
				var hiddenCarac = document.createElement('input');
	
				hiddenCarac.setAttribute("id", "urlsForm.extratores["+j+"]");
				hiddenCarac.setAttribute("name", "urlsForm.extratores");
				hiddenCarac.setAttribute("type", "hidden");
				hiddenCarac.setAttribute("value", caracteristicas[i].value);
				
				document.getElementById('imagensForm').appendChild(hiddenCarac);
				j++;
		    }
		}
		caracteristicas = document.getElementsByName("extratores_textura");
		for (var i = 0; i < caracteristicas.length; i++) { 
		    if (caracteristicas[i].checked) { 
				var hiddenCarac = document.createElement('input');
	
				hiddenCarac.setAttribute("id", "urlsForm.extratores["+j+"]");
				hiddenCarac.setAttribute("name", "urlsForm.extratores");
				hiddenCarac.setAttribute("type", "hidden");
				hiddenCarac.setAttribute("value", caracteristicas[i].value);
				
				document.getElementById('imagensForm').appendChild(hiddenCarac);
				j++;
		    }
		}
		caracteristicas = document.getElementsByName("extratores_forma");
		for (var i = 0; i < caracteristicas.length; i++) { 
		    if (caracteristicas[i].checked) { 
				var hiddenCarac = document.createElement('input');
	
				hiddenCarac.setAttribute("id", "urlsForm.extratores["+j+"]");
				hiddenCarac.setAttribute("name", "urlsForm.extratores");
				hiddenCarac.setAttribute("type", "hidden");
				hiddenCarac.setAttribute("value", caracteristicas[i].value);
				
				document.getElementById('imagensForm').appendChild(hiddenCarac);
				j++;
		    }
		}
	
		document.getElementById("imagensForm").submit();
	}
}

function validarForm(){
	var msg = "";
	var checkedOK = false;
	
	var form = document.getElementsByName("caracteristica");

	for (i = 0; i < form.length; i++) { 
	    if (form[i].checked) { 
	    	checkedOK = true;
	    	break;
	    }
	}
	
	if(!checkedOK){
		msg += "Selecione ao menos uma característica para a busca!\n";
	}else{
		checkedOK = false;
		var caracteristicas = document.getElementsByName("extratores_cor");
		for (var i = 0; i < caracteristicas.length; i++) { 
		    if (caracteristicas[i].checked) { 
		    	checkedOK = true;
		    	break;
		    }
		}
		if(!checkedOK){
			caracteristicas = document.getElementsByName("extratores_textura");
			for (var i = 0; i < caracteristicas.length; i++) { 
			    if (caracteristicas[i].checked) { 
			    	checkedOK = true;
			    	break;
			    }
			}
		}
		if(!checkedOK){
			caracteristicas = document.getElementsByName("extratores_forma");
			for (var i = 0; i < caracteristicas.length; i++) { 
			    if (caracteristicas[i].checked) { 
			    	checkedOK = true;
			    	break;
			    }
			}
		}
		
		if(!checkedOK){
			msg += "Selecione ao menos um extrator para a busca!\n";
		}
	}
	if(document.getElementById("num_resultados").value == "0"){
		msg += "Selecione o número de resultados a ser retornado!\n";
	}
	if(msg.length > 0){
		alert(msg);
		return false;
	}
	return true;
}

function mostrarDescritores(inputExtrator, descritor){
	var count = 1;
	var form = document.getElementsByName("caracteristica");
	var check = false;

	for (i = 0; i < form.length; i++) { 
	    if (form[i].checked) { 
	    	 check = true;
	    }
	}
	
	if(check){
		document.getElementById("desc_extrator").style.display = "block";
	}else{
		document.getElementById("desc_extrator").style.display = "none";
	}
	if(inputExtrator.checked){
		document.getElementById("extrator" + descritor).style.display = "block";
		
		var extratores = document.getElementsByName("extratores" + descritor);
		
		for (i = 0; i < extratores.length; i++) { 
			extratores[i].checked = true;
		}
	}else{
		document.getElementById("extrator" + descritor).style.display = "none";
		
		var extratores = document.getElementsByName("extratores" + descritor);
		
		for (i = 0; i < extratores.length; i++) { 
			extratores[i].checked = false;
		}
	}
	count++;

}