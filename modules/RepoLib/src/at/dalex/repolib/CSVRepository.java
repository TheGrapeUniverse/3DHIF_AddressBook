package at.dalex.repolib;

import at.dalex.repolib.model.IdentityObject;

import java.io.*;
import java.util.Iterator;

public abstract class CSVRepository<T extends IdentityObject> extends RepositoryBase<T> {

    protected abstract String   getHeader();
    protected abstract String   convertModelToCSV(T model);
    protected abstract T        convertCSVToModel(String csvLine);

    public CSVRepository(Class<T> modelClass) {
        super(modelClass);
    }

    public void readCSVFile(String csvPath) {
        File csvFile = new File(csvPath);
        if (!csvFile.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            reader.readLine(); //Skip header

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                T model = convertCSVToModel(currentLine);
                insert(model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeCSVFile(String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputFile)))) {
            writer.write(getHeader() + "\n");
            for (Iterator<T> it = getAll(); it.hasNext(); ) {
                T model = it.next();
                EntryState modelState = getModelState(model);

                if (modelState != EntryState.DELETED) {
                    writer.write(convertModelToCSV(model) + "\n");
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
