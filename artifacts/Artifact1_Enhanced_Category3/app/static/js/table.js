var table;

// constant for computing age in years from age in weeks
const weeksPerYear = 365.25 / 7;

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
			    { title: "DOB", field: "date_of_birth", formatter: "datetime", formatterParams: {
				    inputFormat: "iso",
				    outputFormat: "M/d/yyyy",
				    invalidPlaceholder: "",
				}},
			    { title: "Intake date/time", field: "datetime", formatter: "datetime", formatterParams: {
				    inputFormat: "iso",
				    outputFormat: "M/d/yyyy H:mm:ss",
				    invalidPlaceholder: "",
				}},
			    { title: "Outcome type", field: "outcome_type" },
			    { title: "(Subtype)", field: "outcome_subtype" },
			    { title: "Age upon outcome (years)", field: "age_upon_outcome_in_weeks",
					formatter: cell => {
						var weeks = cell.getValue();
						return weeks == null ? "" : (weeks / weeksPerYear).toFixed(1);
					}
				},
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
			updateAnimalLabel(row.getData());
			updateDropdowns(row.getData());
		});
		table.on("rowDeselected", () => {
			updateDropdowns(null);
			updateAnimalLabel(null);
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
			updateAnimalLabel(null);
			updateDropdowns(null);
			drawPiechart(animalData);
		})
		.catch(error => console.error("Error fetching animal data:", error));
});
