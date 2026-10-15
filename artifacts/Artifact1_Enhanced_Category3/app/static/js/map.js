var mapPin;
// default view set to Austin, TX, USA
var selectedAnimalMap = L.map('map-id').setView([30.45, -97.70], 9);

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

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(selectedAnimalMap);
