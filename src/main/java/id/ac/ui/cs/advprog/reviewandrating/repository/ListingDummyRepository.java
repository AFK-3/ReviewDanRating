package id.ac.ui.cs.advprog.reviewandrating.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Repository
public class ListingDummyRepository {
    Map<String, String> dummyData = new HashMap();

    public ListingDummyRepository () {
        dummyData.put("1", "Apple");
        dummyData.put("2", "Fork");
        dummyData.put("3", "Table");
        dummyData.put("4", "Something");
        dummyData.put("5", "Idk");
    }

    public String findById(String id) {
        return dummyData.get(id);
    }
}
