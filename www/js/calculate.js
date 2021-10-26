//округление числа (число, степень округления)
function roundNumber(num, scale) {
	if(!("" + num).includes("e")) {
		return +(Math.round(num + "e+" + scale)  + "e-" + scale);
	} else {
		var arr = ("" + num).split("e");
		var sig = ""
		if(+arr[1] + scale > 0) {
			sig = "+";
		}
		return +(Math.round(+arr[0] + "e" + sig + (+arr[1] + scale)) + "e-" + scale);
	}
}
//запись тарифов в скрытые теги <p> с указанием нужной таблицы и адреса api
function writeRatesToHiddenTags(){
	//получение id таблицы из выпадающего меню
	let ratesId = $(".rates__selector").val();
	let ratesGetterUrl = '/api/get_prices?rates_set_id=' + ratesId;
	importJsonToInfoPage(ratesGetterUrl);
}

function calculate() {
	let height = $('.height__input').val();
	let width = $('.width__input').val();
	let sashesCount = $('.rates__selector').val();
	let glazingMode = $('.glazing__selector').val();

	if (glazingMode == 1) {
		glazingCoef = parseFloat($('.single__glazing__price').html());
	}

	if (glazingMode == 2) {
		glazingCoef = parseFloat($('.double__glazing__price').html());
	}

	if (glazingMode == 3) {
		glazingCoef = parseFloat($('.triple__glazing__price').html());
	}

	let rawResult;
	let result;

	//Математеческая модель
	rawResult = height * width * sashesCount * glazingCoef / 100;
	//Минимальная сумма расчёта — 1000 руб
	(rawResult < 1000) ? result = 1000 : result = roundNumber(rawResult, 2);
	$('.result__span').html(result);

}

//генерация PDF для однорежимных счётчиков
function createPdf(tokenCookie){

	tokenCookie = getCookie("token");
	if (!tokenCookie) tokenCookie = 0;

	let width = $(".width__input").val();
	let height = $(".height__input").val();
	let sashesCount = $('.rates__selector').val();
	let glazing = $('.glazing__selector').val();
	let totalAmount = $(".result__span").html();

	$.ajax({
		url: '/api/generate_pdf',     
		type: 'GET',
		data : {
            token : tokenCookie,
            width : width,
            height : height,
            sashesCount : sashesCount,
            glazing : glazing,
            totalAmount : totalAmount
        },
        dataType: 'json',                   
        success: function(data)
        {
        	let pdfDocDownloadLink = "http://" + location.host  + data.responseBody;
        	alert("Отчёт сформирован. Он откроется в новом окне");
        	window.open(pdfDocDownloadLink, '_blank');
        },
        statusCode: {
        	400: function (response) {
        		alert("Недопустимые данные");
        	},
        	500: function (response) {
        		alert("Произошла ошибка");
        	}
        },
    })

}

let successfulCalculation = false;

//слушатели (обновление результатов при взаимодействии):
//выпадающее меню выбора тарифных ставок
$('.rates__selector').change(function() {
	writeRatesToHiddenTags();
	successfulCalculation = false;

});

//кнопка расчёта
$('.calculate__button').click(function() {
	let ratesId = $('.rates__selector').val();
	let meterMode = $('.glazing__selector').val();
	if (ratesId == 0 || meterMode == 0){
		alert ("Сконфигурируйте калькулятор");
	} else {
		// проверка на ввод букв
		let number1 = $('.height__input').val();
		let number2 = $('.width__input').val();
        if($.isNumeric(number1) && $.isNumeric(number2)){
                calculate();
				successfulCalculation = true;
         }else{
            alert ("Можно вводить только числа")
         }
		
	}
});

//кнопка PDF отчёта
$('.report__button').click(function() {
	let ratesId = $('.rates__selector').val();
	let meterMode = $('.meter__mode__selector').val();
	if (successfulCalculation){
		createPdf(tokenCookie);
	} else {
		alert ("Сначала нужно произвести расчёт");
	}
});