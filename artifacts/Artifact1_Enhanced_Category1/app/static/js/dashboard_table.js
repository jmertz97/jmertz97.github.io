var table;
var mapPin;
// default view set to Austin, TX, USA
var selectedAnimalMap = L.map('map-id').setView([30.45, -97.70], 9);


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
        title: 'Outcome Types',
	};

	Plotly.newPlot('graph-id', piechart_data, piechart_layout);
}


function updateMap(selectedAnimal, map) {
    var latitude = selectedAnimal.location_lat;
    var longitude = selectedAnimal.location_long;

	// reposition existing map pin if available, otherwise create it
	if (mapPin) {
        mapPin.setLatLng([latitude, longitude]);
    } else {
        mapPin = L.marker([latitude, longitude]).addTo(map);
    }
	
	// (re)label the map pin
    mapPin.bindPopup(
		"<p><b style='display:block; text-align:center'>"+ (selectedAnimal.name || selectedAnimal.animal_id) + "</b><br>" 
		+ "Type: " + (selectedAnimal.animal_type || "Unknown") + "<br>"
		+ "Breed: " + (selectedAnimal.breed || "Unknown") + "<br>"
		+ "Outcome: " + (selectedAnimal.outcome_type || "")
		+ (selectedAnimal.outcome_subtype ? " (" + selectedAnimal.outcome_subtype + ")" : "")
		+ "</p>"
	);

	// snap map window to pin
    map.setView([latitude, longitude], map.getZoom());
}

// fetch table data from the /api/animals route
fetch("/api/animals")
	.then(response => response.json())
	.then(animalData => {
		table = new Tabulator("#datatable-id", {
			columns: [
				// columns are hardcoded for now
			    { title: "ID", field: "animal_id" },
			    { title: "Name", field: "name" },
			    { title: "Type", field: "animal_type" },
			    { title: "Breed", field: "breed" },
			    { title: "Color", field: "color" },
			    { title: "DOB", field: "date_of_birth" },
			    { title: "Datetime", field: "datetime" },
			    { title: "Outcome type", field: "outcome_type" },
			    { title: "(Subtype)", field: "outcome_subtype" },
			    { title: "Age upon outcome", field: "age_upon_outcome" },
			    { title: "Sex upon outcome", field: "sex_upon_outcome" },
			    { title: "Location (Lat.)", field: "location_lat" },
			    { title: "Location (Long.)", field: "location_long" },
			],
			data: animalData,
		    selectableRows: 1,
		    headerSort: true,
		    pagination: true,
		    paginationSize: 10,
		    layout: "fitColumns",
		});		
		table.on("rowSelected", function(row) {
			// update the map when a new row is selected
			var selectedAnimal = row.getData();
			updateMap(selectedAnimal, selectedAnimalMap);
		});
		drawPiechart(animalData);  // draw initial piechart
	})
	.catch(error => console.error("Error fetching animal data:", error));

// listener for category selection dropdown element
document.getElementById("dropdown-id").addEventListener("change", function () {
	var selection = this.value;
	var url = "/api/animals";
	if (selection) {
		// category is passed in HTTP request via query parameter
		url += "?category=" + encodeURIComponent(selection);
	}

	fetch(url)
		.then(response => response.json())
		.then(animalData => {
			// update table and chart based on new query's result
			table.setData(animalData);
			drawPiechart(animalData);
		})
		.catch(error => console.error("Error fetching animal data:", error));
});

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(selectedAnimalMap);
