import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class StageReader {
  public static class BadStageFormatException extends RuntimeException {
    public BadStageFormatException(String message) {
      super(message);
    }
  }

  public static Stage readStage(String path) throws IOException {
    Stage stage = new Stage();
    List<String> lines = Files.readAllLines(Paths.get(path));
    
    for (String line : lines) {
      String[] parts = line.split("=");
      if (parts.length != 2) {
        throw new BadStageFormatException("Invalid line format: " + line);
      }

      String location = parts[0];
      String actorType = parts[1];

      char colLabel = location.charAt(0);
      int row;
      try {
        row = Integer.parseInt(location.substring(1));
      } catch (NumberFormatException e) {
        throw new BadStageFormatException("Invalid row number: " + location);
      }
      
      Optional<Cell> cell = stage.grid.cellAtColRow(colLabel, row);
      if (cell.isEmpty()) {
        throw new BadStageFormatException("Cell location out of bounds: " + location);
      }

      switch (actorType) {
        case "cat":
          stage.actors.add(new Cat(cell.get()));
          break;
        case "dog":
          stage.actors.add(new Dog(cell.get()));
          break;
        case "bird":
          stage.actors.add(new Bird(cell.get()));
          break;
        default:
          throw new BadStageFormatException("Invalid actor type: " + actorType);
      }
    }
    
    return stage;
  }
}