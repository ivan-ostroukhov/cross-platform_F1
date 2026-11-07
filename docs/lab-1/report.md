# Отчёт по лабораторной работе №1

## 1. Описание работы

**Цель:** Сборка каркаса кроссплатформенного приложения F1Archive на основе Compose Multiplatform с поддержкой нескольких платформ (Android, Desktop, Web).

Каркас приложения обеспечивает:
- Переключение темы (светлая / тёмная) через Material 3 токены.
- Смену языка (русский / английский) через `composeResources`.
- Навигацию между списком, деталями гонки и деталями трассы с анимациями переходов и возвратов.
- Отображение моковых данных (20 записей гонок и трасс).

---

## 2. Структура проекта (Package by Layer)

Проект структурирован по слоям в модуле `:shared`:

```text
com.example.apex/
├── data/                       # Данные и репозитории
│   ├── RaceRepository.kt
│   └── MockRaceRepository.kt
├── domain/                     # Сущности предметной области
│   ├── Race.kt
│   ├── Circuit.kt
│   ├── Driver.kt
│   └── RaceDetails.kt
├── navigation/                 # Маршруты и навигация
│   └── Route.kt
├── presentation/               # UI и интерфейс пользователя
│   ├── locale/
│   │   ├── AppLanguage.kt
│   │   └── LocalAppLocale.kt
│   ├── theme/
│   │   └── AppTheme.kt
│   ├── screens/
│   │   ├── RaceListScreen.kt
│   │   ├── RaceDetailsScreen.kt
│   │   ├── CircuitDetailsScreen.kt
│   │   └── components/
│   │       └── RaceCard.kt
│   └── viewmodels/
│       ├── RaceListViewModel.kt
│       ├── RaceDetailsViewModel.kt
│       └── CircuitDetailsViewModel.kt
└── App.kt                      # Главная точка входа CMP
```

---

## 3. Навигация (Navigation 3)

Использована официальная мультиплатформенная библиотека `org.jetbrains.androidx.navigation3:navigation3-ui`.

Маршруты объявлены через `sealed interface Route`:
- `Route.RaceList` — `data object` для списка гонок.
- `Route.RaceDetails(val raceId: Int)` — `data class` с ID гонки.
- `Route.CircuitDetails(val circuitId: Int)` — `data class` с ID трассы.

Для анимирования переходов настроены пары `transitionSpec` (переход вперёд) и `popTransitionSpec` (возврат назад).

---

## 4. Локализация и ресурсы

Подписи вынесены в `composeResources`:
- `values/strings.xml` (English)
- `values-ru/strings.xml` (Русский)

Паритет ключей проверяется скриптом `tools/check-strings.py`.
Каталог данных хранит значения на языке источника, а интерфейс полностью локализован.

---

## 5. Тематизация

Тематизация выстроена на токенах `MaterialTheme` (схемы `lightColorScheme()` и `darkColorScheme()`). На поверхностях отсутствуют жестко зашитые цвета.

---

## 6. Запуск проекта

### Desktop (JVM)
```bash
./gradlew :desktopApp:run
```

### Проверка паритета строк
```bash
python tools/check-strings.py
```
