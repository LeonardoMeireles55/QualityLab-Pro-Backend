package leonardo.labutilities.labcontrol.controller;
import leonardo.labutilities.labcontrol.analitics.Analitics;
import leonardo.labutilities.labcontrol.analitics.AnaliticsList;
import leonardo.labutilities.labcontrol.main.AnaliticRepository;
import leonardo.labutilities.labcontrol.records.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analitics")
public class ExanController {
    @Autowired
    private AnaliticRepository analiticRepository;

    @GetMapping
    @RequestMapping("/getresults")
    public List<AnaliticsList> getResults() {
        return analiticRepository.findAll().stream().map(AnaliticsList::new).toList();
    }

    @PostMapping
    @Transactional
    @RequestMapping("/sendValues")
    public void sendValues(@RequestBody ValuesOfLevels values) {
        analiticRepository.save(new Analitics(values));

    }

}
