package leonardo.labutilities.qualitylabpro.components;

import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import org.springframework.stereotype.Component;

@Component
public class LevelConverterComponent {

   public String convertLevel(String inputLevel) {
      return switch (inputLevel) {
         case "1" -> "PCCC1";
         case "2" -> "PCCC2";
         case "normal" -> "Normal C. Assayed";
         case "low" -> "Low C. Assayed";
         default -> throw new ErrorHandling.ResourceNotFoundException("Level not found.");
      };
   }

   public String convertLevelACL(String inputLevel) {
      return switch (inputLevel) {
         case "1" -> "Normal C. Assayed";
         case "2" -> "Low Abn C. Assayed";
         default -> throw new ErrorHandling.ResourceNotFoundException("Level not found.");
      };
   }
}
