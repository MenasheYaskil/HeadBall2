# HeadBall — Arcade 1v1 Soccer (Java/Swing)

A lightweight, responsive 2D head-soccer game built with Java/Swing.
The project keeps a **fixed logical world** and scales cleanly to any window size, including **borderless fullscreen**. Physics are tuned for a snappy arcade feel; collisions are precise; audio is robust across common WAV formats.

---

## ✨ Features

* **Fixed logical resolution**: `800×381` for stable physics/collisions
* **Pixel-perfect scaling**: letterboxed render with preserved aspect ratio
* **Borderless fullscreen**: toggle with `F11`, exit with `ESC` (no freezes)
* **Tuned physics**: balanced run/jump vs. ball velocity & gravity
* **Precise collisions**: per-player head radius + post-impact separation
* **Realistic ball look**: soccer texture + dynamic height-based shadow
* **Clean audio engine**: auto-converts WAV to PCM 16-bit at load (no 24-bit errors)
* **Polished screens**: start screen with music & hotkeys, win screen with confetti & vignette
* **Resource-safe**: timers and clips are closed/stopped on exit

---

## 🎮 Controls

| Player | Move Left | Move Right | Jump |
| -----: | :-------- | :--------- | :--- |
|     P1 | ←         | →          | ↑    |
|     P2 | A         | D          | W    |

**Global:**

* **F11** – Toggle fullscreen
* **ESC** – Exit fullscreen / close win screen
* **Start screen:** `Enter` to start, `I` for instructions, `M` mute/unmute music

---

## 📁 Project Structure (key files)

```
src/
  GamePanel.java         // main game panel (scaling, HUD, timers, fullscreen)
  Ball.java              // ball physics, collisions, shadow, hits
  Player1.java           // right player (arrow keys)
  Player2.java           // left player (WAD keys)
  AudioPlayer.java       // robust Clip loader with PCM 16-bit conversion
  StartPanel.java        // start screen, background music, hotkeys
  WinPanel.java          // end screen with confetti & vignette
  Point.java             // simple mutable 2D point
assets/
  images/
    TheBackraund.jpg
    backraundAfterPressed.jpg
    ball_soccer.png      // optional (falls back to red circle if missing)
    green wins.jpg
    red wins.jpg
    draw.jpg
    istroctions.png
  Audio/
    Audio_Voicy_Whistle sound effect.wav
    Audio_jumppp11.wav
    ball_hit.wav
```

> If your project doesn’t use `assets/`, keep the current paths in `images/` and `Audio/` as in the code.

---

## ▶️ Build & Run

**Requirements:** Java 21+ (e.g., GraalVM JDK 21 or OpenJDK 21).
Open in IntelliJ IDEA and run `StartPanel` (or your launcher), **or**:

```bash
# from project root (example)
javac -d out src/*.java
java -cp out StartPanel
```

> If you use packages, adjust paths accordingly.

---

## 🛠 Configuration Quick-Tweaks

**GamePanel.java**

* `WINDOWED_W/H` – default windowed size
* Fixed logical size: `LOGICAL_W/H` (keep as is for consistent physics)

**Ball.java**

* `DIAMETER`, `GRAVITY`, `ELASTICITY`, `MAX_SPEED`, `GROUND_Y`
* Sound cooldown: `≥80ms` in `playHit()`

**Player1.java / Player2.java**

* `RUN_SPEED`, `AIR_CONTROL`, `JUMP_IMPULSE`, `GRAVITY_Y`
* Head collision tuning: `HEAD_OFFSET_X/Y`, `HEAD_RADIUS`
* Ground alignment: `FOOT_OFFSET`

**AudioPlayer.java**

* Works with WAV (any depth); converts to **PCM 16-bit LE** internally
* Volume: `setVolumeDb()` or `setVolumeLinear(0..1)`

---

## 🔊 Audio Notes

* If you previously saw `LineUnavailableException ... 24 bit ... not supported`, this is fixed.
* You can keep 24-bit WAVs; they’re safely converted on load.
* To trim memory for short SFX, prefer mono 44.1kHz PCM 16-bit WAVs.

---

## 🧪 Troubleshooting

* **No sound / format errors**: ensure files exist at the paths used in code. The loader checks classpath first, then disk.
* **Fullscreen issues**: borderless fullscreen avoids FSEM; use **F11** to toggle, **ESC** to exit.
* **Images stretched**: scaling preserves aspect ratio; letterbox bars are expected on non-matching window sizes.
* **Missing textures**: ball falls back to a red circle if `ball_soccer.png` is missing.

---

## 🗺️ Roadmap (nice-to-have)

* Pause menu + settings (volume, difficulty)
* Rebindable keys
* Power-ups and AI opponent
* Score history & replays

---

## 📝 Credits

* Code & design: you (project owner)
* Assets: your images/audio; replace placeholders as needed

---

# HeadBall — משחק כדורגל ארקייד 1v1 (Java/Swing)

משחק 2D קליל ומהיר ב-Java/Swing.
המשחק עובד במרחב לוגי קבוע ומתרנדר נקי לכל גודל חלון, כולל **מסך מלא חסר-מסגרת**. הפיזיקה מלוטשת, הקוליז’נים מדויקים, והאודיו יציב לכל פורמט WAV נפוץ.

---

## ✨ תכונות

* **רזולוציה לוגית קבועה**: `800×381` לפיזיקה/קוליז’נים יציבים
* **סקיילינג נקי**: רינדור עם שמירת יחס (Letterbox)
* **מסך מלא חסר-מסגרת**: `F11` להפעלה, `ESC` ליציאה (ללא תקיעות)
* **פיזיקה מכוונת**: ריצה/קפיצה מאוזנות מול מהירות הכדור
* **קוליז’ן מדויק**: רדיוס ראש פר שחקן + Separation לאחר מגע
* **כדור ריאליסטי**: טקסטורת כדורגל + צל דינמי לפי גובה
* **מנוע אודיו נקי**: המרה אוטומטית ל-PCM 16-ביט (אין שגיאות 24-ביט)
* **מסכים מלוטשים**: מסך פתיחה עם מוזיקה וקיצורי דרך; מסך ניצחון עם קונפטי
* **ניהול משאבים**: עצירת טיימרים וקליפים בעת יציאה

---

## 🎮 שליטה

| שחקן | שמאלה | ימינה | קפיצה |
| ---: | :---- | :---- | :---- |
|   P1 | ←     | →     | ↑     |
|   P2 | A     | D     | W     |

**כללי:**

* **F11** – מסך מלא
* **ESC** – יציאה ממסך מלא / סגירת מסך ניצחון
* **מסך פתיחה:** `Enter` להתחיל, `I` להוראות, `M` השתקה/ביטול השתקה

---

## 📁 מבנה פרויקט (עיקרי)

```
src/
  GamePanel.java         // לוגיקת תצוגה/סקייל/מסך מלא/HUD/טיימרים
  Ball.java              // פיזיקת הכדור, קוליז’נים, צל, סאונד
  Player1.java           // שחקן ימין (חיצים)
  Player2.java           // שחקן שמאל (WAD)
  AudioPlayer.java       // טעינת Clip עם המרה ל-PCM 16-ביט
  StartPanel.java        // מסך פתיחה: רקע, מוזיקה, קיצורי דרך
  WinPanel.java          // מסך ניצחון: קונפטי + וינייט
  Point.java             // נקודה 2D פשוטה
assets/
  images/
    TheBackraund.jpg
    backraundAfterPressed.jpg
    ball_soccer.png
    green wins.jpg
    red wins.jpg
    draw.jpg
    istroctions.png
  Audio/
    Audio_Voicy_Whistle sound effect.wav
    Audio_jumppp11.wav
    ball_hit.wav
```

> אם אינך משתמש בתיקיית `assets/`, השאר את הנתיבים כפי שמופיעים בקוד (`images/`, `Audio/`).

---

## ▶️ בנייה והרצה

**דרישות:** Java 21+ (למשל GraalVM JDK 21 או OpenJDK 21).
פתח ב-IntelliJ והרץ את `StartPanel`, או:

```bash
# משורש הפרויקט (דוגמה)
javac -d out src/*.java
java -cp out StartPanel
```

> אם יש חבילות (packages), עדכן נתיבים בהתאם.

---

## 🛠 כיוונונים מהירים

**GamePanel.java**

* `WINDOWED_W/H` — גודל חלון ברירת־מחדל
* `LOGICAL_W/H` — גודל לוגי קבוע (מומלץ להשאיר)

**Ball.java**

* `DIAMETER`, `GRAVITY`, `ELASTICITY`, `MAX_SPEED`, `GROUND_Y`
* קירור סאונד פגיעה: `≥80ms` ב־`playHit()`

**Player1/Player2**

* `RUN_SPEED`, `AIR_CONTROL`, `JUMP_IMPULSE`, `GRAVITY_Y`
* `HEAD_OFFSET_X/Y`, `HEAD_RADIUS` לכיול פגיעת ראש
* `FOOT_OFFSET` ליישור השחקנים בפתח השער

**AudioPlayer.java**

* תומך WAV עם המרה ל-**PCM 16-ביט LE**
* ווליום: `setVolumeDb()` או `setVolumeLinear(0..1)`

---

## 🔊 הערות אודיו

* שגיאות `LineUnavailableException ... 24 bit ...` — נפתרו.
* אפשר להשאיר קבצי WAV ב-24-ביט; ההמרה נעשית בזמן טעינה.
* לחיסכון בזיכרון עבור SFX קצרים—עדיף מונו 44.1kHz PCM 16-ביט.

---

## 🧪 פתרון תקלות

* **אין קול / שגיאות פורמט**: בדוק שהקבצים קיימים בנתיבים. הטוען מחפש קודם ב-classpath ואז בדיסק.
* **מסך מלא**: משתמשים ב-borderless — `F11` להחלפה, `ESC` ליציאה.
* **תמונות נמתחות**: שמירת יחס מופעלת; פסים (letterbox) צפויים במסכים שאינם תואמים יחס.
* **טקסטורה חסרה**: הכדור נופל חזרה לעיגול אדום אם `ball_soccer.png` אינו קיים.

---

## 🗺️ מפת דרכים

* תפריט Pause + הגדרות (ווליום/קושי)
* מיפוי מקשים
* AI/בוט ו־Power-ups
* היסטוריית תוצאות/ריפליי

---

## 📝 קרדיטים

* קוד ועיצוב:Menashe Yaskilך

---

בהצלחה, ותהנהו/י מהמשחק! 🎮⚽
