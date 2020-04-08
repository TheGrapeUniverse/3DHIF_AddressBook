package at.dalex.repolib;

import at.dalex.repolib.model.IdentityObject;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

class RepositoryBase<T extends IdentityObject> implements Repository<T>, AutoCloseable {

    private Class<T> modelClass;
    private List<T> modelEntries = new ArrayList<>();
    private HashMap<T, EntryState> modelStates = new HashMap<T, EntryState>();

    RepositoryBase(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    @Override
    public void close() throws Exception {
        modelEntries.clear();
    }

    @Override
    public boolean insert(T modelToAdd) {
        if (modelToAdd == null)
            throw new IllegalArgumentException("The model is null");

        boolean inserted = false;
        //Check if already contained by the list
        T modelWithId = getById(modelToAdd.getId());
        if (modelWithId == null) {
            modelEntries.add(modelToAdd);
            modelStates.put(modelToAdd, EntryState.CURRENT);

            inserted = true;
        }
        else System.err.println("[ERROR] (Insert) Duplicate row with id '" + modelToAdd.getId() + "'.");

        return inserted;
    }

    @Override
    public boolean update(T modelToUpdate) {
        if (modelToUpdate == null)
            throw new IllegalArgumentException("The model is null");

        boolean updated = false;
        T currentModel = getById(modelToUpdate.getId());
        if (currentModel != null) {
            int currentIndex = modelEntries.indexOf(currentModel);
            modelEntries.set(currentIndex, modelToUpdate);

            modelStates.remove(currentModel);
            modelStates.put(modelToUpdate, EntryState.MODIFIED);

            updated = true;
        }
        return updated;
    }

    @Override
    public boolean delete(int id) {
        boolean deleted = false;
        T currentModel = getById(id);
        if (currentModel != null) {
            modelEntries.remove(currentModel);
            modelStates.put(currentModel, EntryState.DELETED);

            deleted = true;
        }
        return deleted;
    }

    @Override
    public Iterator<T> getAll() {
        return modelEntries.iterator();
    }

    @Override
    public T getById(int id) {
        T foundModel = null;

        for (int i = 0; i < modelEntries.size() && foundModel == null; i++) {
            T model = modelEntries.get(i);
            if (model.getId() == id) {
                foundModel = model;
            }
        }
        return foundModel;
    }

    @Override
    public int getMaxId() {
        T maxEntry = modelEntries.stream()
                .max(Comparator.comparing(IdentityObject::getId)).orElse(null);
        return maxEntry != null ? maxEntry.getId() : 0;
    }

    @Override
    public T createModel() {
        T createdModel = null;

        try {
            createdModel = modelClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return createdModel;
    }

    protected EntryState getModelState(T model) {
        return modelStates.get(model);
    }
}
