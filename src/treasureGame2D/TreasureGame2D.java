package treasureGame2D;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.List;

public class TreasureGame2D extends PApplet {
// --- Assets ---

   PImage imgWall, imgGuard, imgSpy, imgTile, imgTreasure;

   // --- Tablero ---
   int rowCount = 19;
   int columnCount = 19;
   int tileSize = 36;
   GameBoard board;

   // --- Personajes ---
   Spy spy;
   List<Guard> guards = new ArrayList<>();

   boolean isGameOver = false;

   public static void main(String[] args) {
      PApplet.main("treasureGame2D.TreasureGame2D");
   }

   @Override
   public void settings() {
      size(columnCount * tileSize, rowCount * tileSize);
   }

   @Override
   public void setup() {
      // Cargar assets
      imgWall = loadImage("wall.png");
      imgTile = loadImage("tile.png");
      imgTreasure = loadImage("price.png");

      PImage[] images = {imgWall, imgTile, imgTreasure};
      for (PImage img : images) {
         img.resize(tileSize, tileSize);
      }

      // Inicializar tablero
      String[] tileMap = {
         "XXXXXXXXXXXXXXXXXXX", "X        X        X", "X XX XXX X XXX XX X",
         "X X      G      X X", "X X XX XXXXX XX X X", "X    X       X    X",
         "XXXX XXX X XXX XXXX", "X      X X        X", "X XXXXXX XXXX XXX X",
         "X X    X TX     X X", "X XXXX XXXXXXXX X X", "X        X        X",
         "XXXX XXX X XXX XXXX", "X    X       X    X", "X XX X XXXXX X XX X",
         "X X      V      X X", "X X XXXX X XXXX X X", "X        S        X",
         "XXXXXXXXXXXXXXXXXXX"
      };
      board = new GameBoard(tileMap, tileSize, imgWall, imgTile, imgTreasure);

      // Crear personajes
      PImage guardSheet = loadImage("guardSprite.png");
      for (int r = 0; r < rowCount; r++) {
         for (int c = 0; c < columnCount; c++) {
            char ch = tileMap[r].charAt(c);
            int x = c * tileSize;
            int y = r * tileSize;
            if (ch == 'S') {
               PImage spySheet = loadImage("spySprite.png");
               spy = new Spy(x, y, tileSize, 3, spySheet, 3, 4);
            }
            if (ch == 'G' || ch == 'V') {
               guards.add(new Guard(x, y, tileSize, 2, guardSheet));
            }
         }
      }

   }

   @Override
   public void draw() {
      background(0);
      board.draw(this);

      spy.update(board);
      spy.display(this, imgSpy);

      for (Guard g : guards) {
         g.update(board);
         g.display(this, imgGuard);
         if (spy.checkCollision(g)) {
            isGameOver = true;
         }
      }

      if (board.isTreasureAt(spy.getCenterX(), spy.getCenterY())) {
         isGameOver = true;
      }

      if (isGameOver) {
         if (board.isTreasureAt(spy.getCenterX(), spy.getCenterY())) {
            showVictoryScreen();
         } else {
            showGameOverScreen();
         }
         noLoop();
      }
   }

   @Override
   public void keyPressed() {
      spy.keyPressed(key, keyCode);
      if (key == 'r' || key == 'R') {
         resetGame();
      }
   }

   @Override
   public void keyReleased() {
      spy.keyReleased();
   }

   public void resetGame() {
      spy.reset();
      for (Guard g : guards) {
         g.reset();
      }
      isGameOver = false;
      loop();
   }

   // --- Pantallas ---
   public void showVictoryScreen() {
      background(35, 102, 30);
      textSize(50);
      textAlign(CENTER);
      fill(255, 215, 0);
      text("¡MISIÓN CUMPLIDA!", width / 2, height / 2);
      textSize(20);
      fill(255);
      text("Has recuperado el tesoro. Pulsa R para jugar de nuevo", width / 2, height / 2 + 50);
   }

   public void showGameOverScreen() {
      background(12, 12, 12);
      textSize(50);
      textAlign(CENTER);
      fill(255);
      text("¡GAME OVER!", width / 2, height / 2);
      textSize(20);
      text("Pulsa R para reiniciar", width / 2, height / 2 + 50);
   }

   // ===================== CLASES =====================
   class GameBoard {

      String[] map;
      int tileSize;
      PImage wall, floor, treasure;

      GameBoard(String[] map, int tileSize, PImage wall, PImage floor, PImage treasure) {
         this.map = map;
         this.tileSize = tileSize;
         this.wall = wall;
         this.floor = floor;
         this.treasure = treasure;
      }

      void draw(PApplet app) {
         for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[r].length(); c++) {
               int x = c * tileSize;
               int y = r * tileSize;
               char ch = map[r].charAt(c);
               if (ch == 'X') {
                  app.image(wall, x, y);
               } else {
                  app.image(floor, x, y);
               }
               if (ch == 'T') {
                  app.image(treasure, x, y);
               }
            }
         }
      }

      boolean isWallAt(int x, int y) {
         int c = constrain(x / tileSize, 0, columnCount - 1);
         int r = constrain(y / tileSize, 0, rowCount - 1);
         return map[r].charAt(c) == 'X';
      }

      boolean isTreasureAt(int x, int y) {
         int c = constrain(x / tileSize, 0, columnCount - 1);
         int r = constrain(y / tileSize, 0, rowCount - 1);
         return map[r].charAt(c) == 'T';
      }
   }

   abstract class Character {

      int x, y, startX, startY, size, velocity;

      Character(int x, int y, int size, int velocity) {
         this.x = x;
         this.y = y;
         this.startX = x;
         this.startY = y;
         this.size = size;
         this.velocity = velocity;
      }

      void reset() {
         x = startX;
         y = startY;
      }

      int getCenterX() {
         return x + size / 2;
      }

      int getCenterY() {
         return y + size / 2;
      }

      abstract void update(GameBoard board);

      abstract void display(PApplet app, PImage img);
   }

   class Spy extends Character {

      PImage[][] sprites; // sprites[direction][frame]
      int currentFrame = 0;
      int frameDelay = 10;
      int frameCounter = 0;

      char direction = 'D'; // inicial hacia abajo
      boolean isMoving = false;
      int margin = 8;

      Spy(int x, int y, int size, int velocity, PImage spriteSheet, int frames, int directions) {
         super(x, y, size, velocity);
         loadSprites(spriteSheet, frames, directions);
      }

      void loadSprites(PImage sheet, int frames, int directions) {
         sprites = new PImage[directions][frames]; // ahora [fila=dirección][col=frame]
         int w = sheet.width / frames;   // ancho de cada frame
         int h = sheet.height / directions; // alto de cada dirección

         for (int d = 0; d < directions; d++) {
            for (int f = 0; f < frames; f++) {
               sprites[d][f] = sheet.get(f * w, d * h, w, h);
               sprites[d][f].resize(size, size);
            }
         }
      }

      void keyPressed(char k, int code) {
         isMoving = true;
         if (code == UP || k == 'w') {
            direction = 'U';
         }
         if (code == DOWN || k == 's') {
            direction = 'D';
         }
         if (code == LEFT || k == 'a') {
            direction = 'L';
         }
         if (code == RIGHT || k == 'd') {
            direction = 'R';
         }
      }

      void keyReleased() {
         isMoving = false;
         currentFrame = 0; // frame de reposo
      }

      @Override
      void update(GameBoard board) {
         if (!isMoving) {
            return;
         }

         int nextX = x;
         int nextY = y;

         switch (direction) {
            case 'U':
               nextY -= velocity;
               break;
            case 'D':
               nextY += velocity;
               break;
            case 'L':
               nextX -= velocity;
               break;
            case 'R':
               nextX += velocity;
               break;
         }

         if (!checkCollisionWithWalls(nextX, nextY, board)) {
            x = nextX;
            y = nextY;
         }

         adjustCorners();

         // Animación
         frameCounter++;
         if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % sprites[0].length; // 3 frames
            frameCounter = 0;
         }
      }

      void adjustCorners() {
         if (direction == 'U' || direction == 'D') {
            int errorX = x % size;
            if (errorX > 0 && errorX <= margin) {
               x -= errorX;
            } else if (errorX >= size - margin) {
               x += size - errorX;
            }
         } else {
            int errorY = y % size;
            if (errorY > 0 && errorY <= margin) {
               y -= errorY;
            } else if (errorY >= size - margin) {
               y += size - errorY;
            }
         }
      }

      boolean checkCollisionWithWalls(int nextX, int nextY, GameBoard board) {
         int left = nextX;
         int right = nextX + size - 1;
         int up = nextY;
         int down = nextY + size - 1;

         switch (direction) {
            case 'U':
               return board.isWallAt(left, up) || board.isWallAt(right, up);
            case 'D':
               return board.isWallAt(left, down) || board.isWallAt(right, down);
            case 'L':
               return board.isWallAt(left, up) || board.isWallAt(left, down);
            case 'R':
               return board.isWallAt(right, up) || board.isWallAt(right, down);
         }
         return false;
      }

      boolean checkCollision(Guard g) {
         int margen = 5;
         return x < g.x + size - margen && x + size > g.x + margen
                 && y < g.y + size - margen && y + size > g.y + margen;
      }

      @Override
      void display(PApplet app, PImage img) {
         int dirIndex = 0;
         switch (direction) {
            case 'D':
               dirIndex = 0;
               break; // fila 0
            case 'L':
               dirIndex = 1;
               break; // fila 1
            case 'R':
               dirIndex = 2;
               break; // fila 2
            case 'U':
               dirIndex = 3;
               break; // fila 3
         }

         app.image(sprites[dirIndex][currentFrame], x, y);
      }
   }

   class Guard extends Character {

      PImage[][] sprites;  // sprites[direccion][frame]
      int currentFrame = 0;
      int frameDelay = 15;
      int frameCounter = 0;

      char direction;  // 'U', 'D', 'L', 'R'
      char[] directions = {'U', 'R', 'D', 'L'};

      Guard(int x, int y, int size, int velocity, PImage spriteSheet) {
         super(x, y, size, velocity);
         loadSprites(spriteSheet, 4, 4); // 4 frames, 4 direcciones
         direction = directions[(int) random(4)]; // dirección inicial aleatoria
      }

      void loadSprites(PImage sheet, int frames, int directions) {
         sprites = new PImage[directions][frames];
         int w = sheet.width / frames;    // ancho de cada frame
         int h = sheet.height / directions; // alto de cada fila

         for (int d = 0; d < directions; d++) {
            for (int f = 0; f < frames; f++) {
               sprites[d][f] = sheet.get(f * w, d * h, w, h);
               sprites[d][f].resize(size, size);
            }
         }
      }

      @Override
      void update(GameBoard board) {
         int nextX = x;
         int nextY = y;

         // Mover según la dirección actual
         switch (direction) {
            case 'U':
               nextY -= velocity;
               break;
            case 'D':
               nextY += velocity;
               break;
            case 'L':
               nextX -= velocity;
               break;
            case 'R':
               nextX += velocity;
               break;
         }

         // Colisión con paredes
         if (checkCollisionWithWalls(nextX, nextY, board)) {
            direction = directions[(int) random(4)]; // cambiar dirección aleatoria
         } else {
            x = nextX;
            y = nextY;
         }

         // Animación
         frameCounter++;
         if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % sprites[0].length; // 4 frames
            frameCounter = 0;
         }
      }

      boolean checkCollisionWithWalls(int nextX, int nextY, GameBoard board) {
         int left = nextX;
         int right = nextX + size - 1;
         int up = nextY;
         int down = nextY + size - 1;

         switch (direction) {
            case 'U':
               return board.isWallAt(left, up) || board.isWallAt(right, up);
            case 'D':
               return board.isWallAt(left, down) || board.isWallAt(right, down);
            case 'L':
               return board.isWallAt(left, up) || board.isWallAt(left, down);
            case 'R':
               return board.isWallAt(right, up) || board.isWallAt(right, down);
         }
         return false;
      }

      @Override
      void display(PApplet app, PImage img) {
         int dirIndex = 0;
         switch (direction) {
            case 'D':
               dirIndex = 0;
               break; // fila 0 = abajo
            case 'L':
               dirIndex = 1;
               break; // fila 1 = derecha
            case 'R':
               dirIndex = 2;
               break; // fila 2 = izquierda
            case 'U':
               dirIndex = 3;
               break; // fila 3 = arriba
         }
         app.image(sprites[dirIndex][currentFrame], x, y);
      }
   }

}
