# MLBB Tracker - Mobile Legends: Bang Bang Timer & Hero Catalog

A modern Android app for Mobile Legends: Bang Bang players featuring a game-friendly overlay timer system and offline hero ultimate catalog. Built with Kotlin and Jetpack Compose following clean architecture principles.

## ✨ Features

### ⏱️ Timer System
- **5 Circular Timers**: GOLD, EXP, JUNGLE, MID, ROAM arranged in 3+2 grid
- **Battle Spell Selection**: Choose from 12 different battle spells with accurate cooldowns
- **PYT Support**: 20% cooldown reduction for MID and ROAM lanes when "Has PYT" is enabled
- **Visual Indicators**: 
  - Red hourglass (full) when counting down
  - Green hourglass (empty) when ready
- **Overlay Mode**: Floating hourglass icons that stay on top of other apps

### 🦸 Hero Catalog
- **5-Column Grid**: Compact display of all heroes
- **Expandable Cards**: Tap to reveal ultimate cooldown information
- **Offline Support**: All data loaded from local JSON assets
- **Alphabetical Sorting**: Easy to find specific heroes

## 🏗️ Architecture

### Clean Architecture Pattern
- **Data Layer**: Models, repositories, and data sources
- **Domain Layer**: Business logic and use cases  
- **Presentation Layer**: UI components and ViewModels

### Key Components
- **ViewModels**: TimerViewModel, HeroesViewModel
- **Repositories**: HeroRepository, SpellRepository
- **Services**: OverlayService for floating overlay
- **Utils**: TimerUtils, Constants for shared functionality

## 🛠️ Technical Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Repository pattern
- **State Management**: StateFlow and Compose state
- **Coroutines**: For asynchronous operations
- **Material Design 3**: Modern UI components

## 📁 Project Structure
```
app/src/main/java/com/example/kotlinandroidoverlayapponhomescreen/
├── data/
│   ├── Models.kt              # Data classes
│   └── Spells.kt              # Battle spell definitions
├── repository/
│   ├── HeroRepository.kt      # Hero data management
│   └── SpellRepository.kt     # Spell data management
├── ui/
│   ├── screens/
│   │   ├── MainScreen.kt      # Main tab container
│   │   ├── TimerScreen.kt     # Timer interface
│   │   └── HeroesScreen.kt    # Hero catalog
│   └── theme/
│       ├── Theme.kt           # App theming
│       └── Type.kt            # Typography
├── utils/
│   ├── Constants.kt           # App constants
│   └── TimerUtils.kt          # Timer utilities
├── viewmodel/
│   ├── TimerViewModel.kt      # Timer logic
│   └── HeroesViewModel.kt     # Hero logic
├── overlay/
│   └── OverlayService.kt      # Floating overlay service
└── MainActivity.kt            # Main activity
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+
- Kotlin 1.8+

### Installation
1. Clone the repository
2. Open in Android Studio
3. Sync project with Gradle files
4. Run on device or emulator

### Permissions
- `SYSTEM_ALERT_WINDOW`: For overlay functionality
- `FOREGROUND_SERVICE`: For overlay service

## 📱 Usage

### Timer Tab
1. Tap any timer circle to start/stop countdown
2. Use SET SPELL buttons to change battle spells
3. Enable "Has PYT" for MID/ROAM lanes (20% cooldown reduction)
4. Use SHOW button to enable overlay mode
5. Use RESET button to restore defaults

### Heroes Tab
1. Browse heroes in 5-column grid
2. Tap any hero to expand and see ultimate cooldowns
3. All data is loaded from local JSON assets

### Overlay Mode
1. Grant overlay permission when prompted
2. Floating hourglass icons appear on screen
3. Tap hourglass icons to open app
4. Use HIDE button to disable overlay

## 🎨 Customization

### Adding New Heroes
1. Add hero images to `res/drawable/`
2. Update `assets/heroes.json` with hero data
3. Follow existing JSON structure

### Adding New Spells
1. Add spell images to `res/drawable/`
2. Update `data/Spells.kt` with spell definitions
3. Include cooldown times and display names

### Theming
1. Modify `ui/theme/Theme.kt` for colors
2. Update `ui/theme/Type.kt` for typography
3. Customize Material 3 components

## 🧪 Development

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Maintain consistent formatting

### Testing
- Unit tests for ViewModels
- UI tests for Compose components
- Integration tests for repositories

## 🤝 Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments
- Mobile Legends: Bang Bang for the game inspiration
- Android Jetpack Compose team for the UI framework
- Material Design for the design system