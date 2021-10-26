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
		alert ("Сконфигурируйте калькулятор")
	} else {
		calculate();
		successfulCalculation = true;
	}
});

function calculate() {
	let height = $('.height__input').val();
	let width = $('.width__input').val();
	let rates = $('.rates__selector').val();
	let glazingMode = $('.glazing__selector').val();
	let sashesCount;

	if (rates == 1) {
		sashesCount = parseFloat($('.single__rate__price').html());
	}

	if (rates == 2) {
		sashesCount = parseFloat($('.daily__rate__price').html());
	}

	if (rates == 3) {
		sashesCount = parseFloat($('.night__rate__price').html());
	}

	let rawResult;
	rawResult = height * width * sashesCount * glazingMode;
	result = roundNumber(rawResult, 2);
	$('.result__span').html(result);

}
//кнопка PDF отчёта
$('.info__button').click(function() {
		alert ("Сначала нужно произвести расчёт");
});