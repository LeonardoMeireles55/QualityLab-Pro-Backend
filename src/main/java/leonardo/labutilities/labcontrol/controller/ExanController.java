package leonardo.labutilities.labcontrol.controller;
import leonardo.labutilities.labcontrol.analitics.Analitics;
import leonardo.labutilities.labcontrol.main.AnaliticRepository;
import leonardo.labutilities.labcontrol.records.ValuesOfPotassio;
import leonardo.labutilities.labcontrol.records.ValuesOfSodio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analitics")
public class ExanController {
    @Autowired
    private AnaliticRepository analiticRepository;
    @PostMapping
    @Transactional
    @RequestMapping("/sodio")
    public void cadastrar(@RequestBody ValuesOfSodio values) {
        analiticRepository.save(new Analitics(values));
}
    @PostMapping
    @Transactional
    @RequestMapping("/potassio")
    public void cadastrar(@RequestBody ValuesOfPotassio values) {
        analiticRepository.save(new Analitics(values));
    }

    @GetMapping
    @RequestMapping("/listartudo")
    public List<AnaliticsList> listarAnalitics() {
        return analiticRepository.findAll().stream().map(AnaliticsList::new).toList();
    }
}
