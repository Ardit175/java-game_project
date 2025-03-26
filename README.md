# Maze Game

Ky është një projekt i zhvilluar si detyrë kursi për lëndën "Programim i Orientuar nga Objektet" 2024-2025. Loja është një labirint interaktiv ku lojtarët duhet të mbledhin thesare dhe të gjejnë rrugën për të arritur te dalja.

## Përmbajtja
- [Veçoritë](#veçoritë)
- [Kërkesat e Sistemit](#kërkesat-e-sistemit)
- [Instalimi](#instalimi)
- [Konfigurimi](#konfigurimi)
- [Përdorimi](#përdorimi)
- [Struktura e Projektit](#struktura-e-projektit)
- [Dokumentimi](#dokumentimi)
- [Troubleshooting](#troubleshooting)

## Veçoritë
- Sistem autentifikimi për përdoruesit
- Labirint i gjeneruar dinamikisht
- Thesare të ndryshëm me pikë të ndryshme
- Sistem për ruajtjen dhe ngarkimin e lojës
- Ndërfaqe grafike intuitive
- Sistem pikësh dhe statistikash

## Kërkesat e Sistemit
- Java SE Development Kit (JDK) 8 ose më i ri
- MySQL 8.0 ose më i ri
- Hapësirë minimale në disk: 100MB
- Memorie RAM: Minimumi 512MB

## Instalimi

### 1. Klono repository-n
```bash
git clone https://github.com/Ardit175/java-game_project.git
cd maze-game
```

### 2. Konfiguro MySQL
```bash
# Instalo MySQL në *Mac* duke përdorur Homebrew
brew install mysql
brew services start mysql

# Ekzekuto skriptin e database-it
mysql -u root -p < resources/database/init.sql
```

### 3. Kompilo projektin
```bash
# Krijo direktorinë bin
mkdir bin

# Kompilo të gjitha files
javac -d bin -cp "lib/*" src/**/*.java

# Ekzekuto programin
java -cp "bin:lib/*" main.MazeGame
```

## Konfigurimi

### Database
1. Krijo një file `config.properties` në `resources/`:
```properties
db.url=jdbc:mysql://localhost:3306/maze_game
db.user=your_username
db.password=your_password
```

### Parametrat e Lojës
Mund të modifikoni këto parametra në `GameController.java`:
- Madhësia e labirintit
- Numri i thesareve
- Pikët për secilin thesar

## Përdorimi

### Kontrollet
- Tastet me shigjeta: Lëvizja e lojtarit
- CTRL+S: Ruaj lojën
- CTRL+L: Ngarko lojën
- CTRL+N: Lojë e re
- ESC: Menu

### Pikët
- Monedhë: 10 pikë
- Gur i çmuar: 50 pikë
- Arkë thesari: 100 pikë
- Kurorë: 200 pikë

## Struktura e Projektit
```
MazeGame/
├── src/
│   ├── main/
│   ├── model/
│   ├── controller/
│   ├── gui/
│   ├── auth/
│   └── util/
├── lib/
├── resources/
└── README.md
```

## Dokumentimi
Dokumentacioni i plotë i kodit ndodhet në direktorinë `docs/`. Për të gjeneruar dokumentacionin:
```bash
javadoc -d docs src/**/*.java
```

## Troubleshooting

### Probleme të zakonshme

#### Database Connection Failed
1. Kontrollo nëse MySQL është duke punuar:
```bash
brew services list
```

2. Kontrollo kredencialet në `DatabaseConfig.java`

3. Testo lidhjen manualisht:
```bash
mysql -u your_username -p
```

#### Compilation Errors
1. Sigurohu që ke JDK të instaluar:
```bash
javac -version
```

2. Kontrollo CLASSPATH:
```bash
echo $CLASSPATH
```


## Autorët
- Ardit Palushi

---
