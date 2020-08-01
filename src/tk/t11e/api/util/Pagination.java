package tk.t11e.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Maxlego08
 * https://github.com/Maxlego08/TemplatePlugin
 */
public class Pagination<T> {

    public List<T> paginateReverse(List<T> list, int inventorySize, int page) {
        List<T> currentList = new ArrayList<>();
        if (page == 0)
            page = 1;
        int idStart = list.size() - 1 - ((page - 1) * inventorySize);
        int idEnd = idStart - inventorySize;
        if (idEnd < list.size() - inventorySize && list.size() < inventorySize * page)
            idEnd = -1;
        for (int a = idStart; a != idEnd; a--)
            currentList.add(list.get(a));
        return currentList;
    }

    public List<T> paginateReverse(Map<?, T> map, int inventorySize, int page) {
        List<T> currentList = new ArrayList<>();
        map.forEach((k, v) -> currentList.add(v));
        return paginateReverse(currentList, inventorySize, page);
    }

    public List<T> paginate(Map<?, T> map, int inventorySize, int page) {
        List<T> currentList = new ArrayList<>();
        map.forEach((k, v) -> currentList.add(v));
        return paginate(currentList, inventorySize, page);
    }

    public <K> HashMap<K, T> paginateMap(Map<K, T> map, int inventorySize, int page) {
        List<K> currentList = new ArrayList<>();
        map.forEach((k, v) -> currentList.add(k));
        List<K> paginated = new Pagination<K>().paginate(currentList, inventorySize, page);
        HashMap<K, T> currentMap = new HashMap<>();
        paginated.forEach(k -> currentMap.put(k, map.get(k)));
        return currentMap;
    }

    public List<T> paginate(List<T> list, int inventorySize, int page) {
        List<T> currentList = new ArrayList<>();
        if (page == 0)
            page = 1;
        int idStart = ((page - 1)) * inventorySize;
        int idEnd = idStart + inventorySize;
        if (idEnd > list.size())
            idEnd = list.size();
        for (int a = idStart; a != idEnd; a++)
            currentList.add(list.get(a));
        return currentList;
    }
}