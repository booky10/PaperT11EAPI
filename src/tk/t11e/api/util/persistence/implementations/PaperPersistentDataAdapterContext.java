package tk.t11e.api.util.persistence.implementations;
// Created by booky10 in PaperT11EAPI (22:17 10.07.20)

import tk.t11e.api.util.persistence.interfaces.PersistentDataAdapterContext;

@Deprecated
public class PaperPersistentDataAdapterContext implements PersistentDataAdapterContext {

    private final PaperPersistentDataTypeRegistry registry;

    public PaperPersistentDataAdapterContext(PaperPersistentDataTypeRegistry registry) {
        this.registry = registry;
    }

    @Override
    public PaperPersistentDataContainer newPersistentDataContainer() {
        return new PaperPersistentDataContainer(registry);
    }
}
