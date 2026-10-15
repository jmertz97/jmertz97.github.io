function getCountPerOutcome(animalData) {
	// uses .reduce method to obtain count of records with each category
	// doable with mongoDB query, but more efficient to operate on already queried data
    return animalData.reduce(function (counts, animal) {
        var type = animal.outcome_type || "Unknown";
        counts[type] = (counts[type] || 0) + 1;
        return counts;
    }, {});
}


function drawPiechart(animalData) {
	// using active query result, obtain counts per outcome via helper function
	var count_per_outcome = getCountPerOutcome(animalData);

	// keys used for labels
	var piechart_data = [{
		values: Object.values(count_per_outcome),
		labels: Object.keys(count_per_outcome),
		type: 'pie'
	}];

	var piechart_layout = {
		height: 400,
		width: 500,
        title: { text: 'Outcome Types' }
	};

	Plotly.newPlot('graph-id', piechart_data, piechart_layout);
}
