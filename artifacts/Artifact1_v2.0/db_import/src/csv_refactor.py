# this script applies the alterations that were made between the original aac_shelter_outcomes.csv file and the 
# version of it used by the current iteration of the artifact
import csv

fieldsToDrop = {"index", "age_upon_outcome", "monthyear"}

with (open("aac_shelter_outcomes_original.csv", mode="r", newline="", encoding="utf-8") as ifile,
      open("aac_shelter_outcomes.csv", mode="w", newline="", encoding="utf-8") as ofile):
	reader = csv.DictReader(ifile)
	headerRow = [field for field in reader.fieldnames if field not in fieldsToDrop]
	writer = csv.DictWriter(ofile, fieldnames=headerRow, extrasaction="ignore")
	writer.writeheader()
	for row in reader:
		# "datetime" is more appropriate name for timestamp col, but vals are better formatted in "monthyear"
		row["datetime"] = row["monthyear"]
		writer.writerow(row)
