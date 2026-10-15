# valid outcome types and their valid subtypes, derived from original aac_shelter_outcomes data
VALID_OUTCOME_TYPES = {
	'Adoption': [
		'Foster',
		'Offsite',
		'',
	],
	'Died': [
		'At Vet',
		'Enroute',
		'In Foster',
		'In Kennel',
		'In Surgery',
		'',
	],
	'Disposal': [''],
	'Euthanasia': [
		'Aggressive',
		'At Vet',
		'Behavior',
		'Court/Investigation',
		'Medical',
		'Suffering',
		'Rabies Risk',
		'Underage',
		'',
	],
	'Missing': [
		'In Foster',
		'In Kennel',
		'',
	],
	'Relocate': [''],
	'Return to Owner': [''],
	'Rto-Adopt': [''],
	'Transfer': [
		'Partner',
		'SCRP',
		'Snr',
	],
}

VALID_OUTCOME_SUBTYPES = {s for subs in VALID_OUTCOME_TYPES.values() for s in subs if s}

VALID_SEXES_UPON_OUTCOME = {
	"Intact Male",
	"Intact Female",
	"Neutered Male",
	"Spayed Female",
	"Unknown"
}

