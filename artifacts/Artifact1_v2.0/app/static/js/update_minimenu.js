// constants for the outcome update minimenu
const animalLabel = document.getElementById("selected-animal-label");
const outcomeTypeSelect = document.getElementById("select-outcome-type");
const outcomeSubtypeSelect = document.getElementById("select-outcome-subtype");
const updateButton = document.getElementById("update-outcome-button");
const statusLabel = document.getElementById("update-status");


// get list of options
for (const outcome_type of Object.keys(VALID_OUTCOMES).sort()) {
	outcomeTypeSelect.add(new Option(outcome_type, outcome_type));
}


function updateAnimalLabel(animal) {
	// updates a label in the update minimenu identifying the currently selected animal
	if (animal) {
		animalLabel.textContent = animal.name ? `${animal.animal_id} (${animal.name})` : animal.animal_id;
	}
	else {
		animalLabel.textContent = "[no animal selected]";
	}
}


function fillSubtypes() {
	// clears and rebuilds the subtypes
	const allowed = VALID_OUTCOMES[outcomeTypeSelect.value] || [];
	const subtypes = allowed.filter(s => s !== "");
	const optional = !allowed.includes("");

	outcomeSubtypeSelect.length = 0;
	if (optional || subtypes.length === 0) {
		outcomeSubtypeSelect.add(new Option("(none)", ""));
	}
	subtypes.forEach(s => outcomeSubtypeSelect.add(new Option(s, s)));
	outcomeSubtypeSelect.disabled = subtypes.length === 0;
}


function updateDropdowns(animal) {
	// 
	const unselected = animal == null;
	outcomeTypeSelect.disabled = unselected;
	updateButton.disabled = unselected;

	statusLabel.textContent = "";
	outcomeTypeSelect.value = unselected ? "" : (animal.outcome_type || "");
	fillSubtypes();  // update subtype dropdown based on selected option in type dropdown
	
	if (unselected) {
		outcomeSubtypeSelect.disabled = true;
	}
	else {
		outcomeSubtypeSelect.value = animal.outcome_subtype || "";
	}
}


outcomeTypeSelect.addEventListener("change", fillSubtypes);

updateButton.addEventListener("click", async () => {
	const row = table.getSelectedRows()[0];
	if (!row) return;
	const animal = row.getData();

	if (!outcomeTypeSelect.value) {
		statusLabel.textContent = "Select an outcome first.";
		return;
	}

	statusLabel.textContent = "Updating...";

	try {
		const response = await fetch(`/api/animals/${animal._id}`, {
			method: "PUT",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify({
				outcome_type: outcomeTypeSelect.value,
				outcome_subtype: outcomeSubtypeSelect.value,
			}),
		});

		// empty object fallback to handle json error responses
		const result = await response.json().catch(() => ({}));

		if (!response.ok) {
			statusLabel.textContent = result.error || `Update failed (HTTP ${response.status}).`;
			return;
		}

		// row.update merges, so a removed subtype has to be blanked explicitly
		row.update({ ...result, outcome_subtype: result.outcome_subtype || "" });
		updateMap(result, selectedAnimalMap);
		drawPiechart(table.getData());
		statusLabel.textContent = `Updated ${result.animal_id}.`;
	} catch (error) {
		statusLabel.textContent = "[Server unavailable]";
	} finally {
		updateButton.disabled = table.getSelectedRows().length === 0;
	}
});
