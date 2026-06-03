# Skill Registry — Integrador

## Project-Level Skills

None found.

## User Skills

| Skill | Path | Trigger |
|-------|------|---------|
| agp-9-upgrade | `~/.claude/skills/agp-9-upgrade/SKILL.md` | Upgrading AGP to version 9 (NOT for KMP) |
| edge-to-edge | `~/.claude/skills/edge-to-edge/SKILL.md` | Adding edge-to-edge support, fixing system bar/IME inset issues in Compose |
| mobiai-analyze-crash | `~/.claude/skills/mobiai-analyze-crash/SKILL.md` | User shares a crash (stack trace, log, screenshot) — find root cause |
| mobiai-android-architecture | `~/.claude/skills/mobiai-android-architecture/SKILL.md` | Creating features, refactoring, navigating Android codebase |
| mobiai-android-build | `~/.claude/skills/mobiai-android-build/SKILL.md` | Building Android project, troubleshooting Gradle, configuring flavors |
| mobiai-android-device | `~/.claude/skills/mobiai-android-device/SKILL.md` | Interacting with Android device/emulator via ADB |
| mobiai-android-testing | `~/.claude/skills/mobiai-android-testing/SKILL.md` | Writing/running Android tests (unit, UI) |
| mobiai-brain | `~/.claude/skills/mobiai-brain/SKILL.md` | Asking about past decisions, patterns, or architecture specific to this project |
| mobiai-crashlytics | `~/.claude/skills/mobiai-crashlytics/SKILL.md` | Investigating a Firebase Crashlytics crash link or ID |
| mobiai-create-pr | `~/.claude/skills/mobiai-create-pr/SKILL.md` | BEFORE any git push, branch push, merge, or PR creation |
| mobiai-fix-issue | `~/.claude/skills/mobiai-fix-issue/SKILL.md` | Starting a bug fix from a ticket/issue; re-opening a failed fix |
| mobiai-flutter | `~/.claude/skills/mobiai-flutter/SKILL.md` | Working on Flutter/Dart projects |
| mobiai-graph | `~/.claude/skills/mobiai-graph/SKILL.md` | Code impact, call graph, "where is X used" in mobile codebase |
| mobiai-ios-architecture | `~/.claude/skills/mobiai-ios-architecture/SKILL.md` | Creating features/refactoring iOS codebase |
| mobiai-ios-build | `~/.claude/skills/mobiai-ios-build/SKILL.md` | Building iOS project, troubleshooting, managing deps |
| mobiai-ios-device | `~/.claude/skills/mobiai-ios-device/SKILL.md` | Interacting with iOS Simulator via simctl |
| mobiai-ios-testing | `~/.claude/skills/mobiai-ios-testing/SKILL.md` | Writing/running iOS tests |
| mobiai-kmp | `~/.claude/skills/mobiai-kmp/SKILL.md` | Working on Kotlin Multiplatform projects |
| mobiai-mobile-brainstorming | `~/.claude/skills/mobiai-mobile-brainstorming/SKILL.md` | BEFORE any creative mobile work — explore intent and design |
| mobiai-mobile-debugging | `~/.claude/skills/mobiai-mobile-debugging/SKILL.md` | BEFORE proposing any fix for a mobile bug, test failure, or crash |
| mobiai-mobile-executing-plans | `~/.claude/skills/mobiai-mobile-executing-plans/SKILL.md` | When a written mobile plan exists and you're about to execute it |
| mobiai-mobile-executing-plans-with-subagents | `~/.claude/skills/mobiai-mobile-executing-plans-with-subagents/SKILL.md` | Same as above, but with subagent support available |
| mobiai-mobile-finishing-branch | `~/.claude/skills/mobiai-mobile-finishing-branch/SKILL.md` | Once mobile implementation is finished, before integrating work |
| mobiai-mobile-parallel-agents | `~/.claude/skills/mobiai-mobile-parallel-agents/SKILL.md` | Dispatching agents for 2+ independent mobile problems |
| mobiai-mobile-planning | `~/.claude/skills/mobiai-mobile-planning/SKILL.md` | When a mobile task spans multiple steps/files — before coding |
| mobiai-mobile-tdd | `~/.claude/skills/mobiai-mobile-tdd/SKILL.md` | BEFORE any implementation code — tests first, no exceptions |
| mobiai-mobile-verification | `~/.claude/skills/mobiai-mobile-verification/SKILL.md` | BEFORE declaring work done — fresh verification required |
| mobiai-mobile-worktrees | `~/.claude/skills/mobiai-mobile-worktrees/SKILL.md` | Before starting isolated work via git worktrees |
| mobiai-react-native | `~/.claude/skills/mobiai-react-native/SKILL.md` | Working on React Native projects |
| mobiai-reproduce-bug | `~/.claude/skills/mobiai-reproduce-bug/SKILL.md` | Reproducing a bug on device/emulator/simulator |
| mobiai-review-code | `~/.claude/skills/mobiai-review-code/SKILL.md` | Reviewing mobile code changes |
| mobiai-update | `~/.claude/skills/mobiai-update/SKILL.md` | Updating mobiai binary to latest version |
| mobiai-write-tests | `~/.claude/skills/mobiai-write-tests/SKILL.md` | Writing tests after a fix or for new code |
| mobiai-writing-skills | `~/.claude/skills/mobiai-writing-skills/SKILL.md` | Creating a new MobiAI skill |
| migrate-xml-views-to-jetpack-compose | `~/.claude/skills/migrate-xml-views-to-jetpack-compose/SKILL.md` | Migrating Android XML Views to Jetpack Compose |
| navigation-3 | `~/.claude/skills/navigation-3/SKILL.md` | Installing/migrating to Jetpack Navigation 3 |
| play-billing-library-version-upgrade | `~/.claude/skills/play-billing-library-version-upgrade/SKILL.md` | Upgrading Play Billing Library |
| r8-analyzer | `~/.claude/skills/r8-analyzer/SKILL.md` | Analyzing R8 keep rules for optimization |
| using-mobiai | `~/.claude/skills/using-mobiai/SKILL.md` | Starting any conversation — establishes how to find/invoke MobiAI skills |

## SDD Skills (internal, excluded from skill resolution)

- sdd-init, sdd-explore, sdd-propose, sdd-spec, sdd-design, sdd-tasks, sdd-apply, sdd-verify, sdd-archive, sdd-onboard
- _shared (internal shared references)
- skill-registry (registry management)
- skill-creator (skill creation)

## Project Conventions

No project conventions files found (no CLAUDE.md, AGENTS.md, etc.).

## Compact Rules

### agp-9-upgrade
- Upgrades Android AGP to v9 (NOT for KMP projects)
- Check compatibility: Kotlin, Gradle, namespace DSL, non-transitive R classes
- Update version catalog, sync build files, remove deprecated APIs
- Test build after each change

### edge-to-edge
- Add `enableEdgeToEdge()` in Activity.onCreate BEFORE setContent
- Use `Modifier.safeContentPadding()` or `WindowInsets` APIs
- Handle system bar colors: transparent by default, adjust legibility
- IME insets need `Modifier.imePadding()` for keyboard-aware layouts

### mobiai-analyze-crash
- Read stack trace to identify exact line of crash
- Search codebase for that line plus surrounding context
- Establish root cause with evidence before proposing fix
- Fix + verify + return root cause summary

### mobiai-android-architecture
- Understand project module structure before changing
- Follow existing patterns (MVP, MVVM, MVI, etc.)
- Identify where DI, navigation, and data layers are configured
- Propose changes respecting the existing architecture

### mobiai-android-build
- Gradle build failures: check version catalogs, plugin compatibility
- Flavor/variant issues: verify build config in app/build.gradle.kts
- Dependency conflicts: use `./gradlew :app:dependencies` to debug
- AGP/Kotlin version compatibility is critical

### mobiai-android-device
- ADB commands: install, logcat, screenshots, emulator control
- Use `adb shell` for deeper device interaction
- Capture bug reports with `adb bugreport`
- Manage emulators via avdmanager/emulator CLI

### mobiai-android-testing
- Unit tests: JUnit + Mockito/MockK for Android logic
- UI tests: Compose UI tests with `createComposeRule()`
- Run: `./gradlew test` or specific variant
- Test coverage: JaCoCo or Kover

### mobiai-brain
- Check `<repo>/.mobiai/brain/` for per-project living memory
- Load context about past decisions, patterns, bugfixes
- BEFORE proposing non-trivial architecture changes, consult brain
- Overrides generic best-practices with real project conventions

### mobiai-crashlytics
- Firebase Crashlytics deep investigation
- Fetch crash details via Firebase console API or link
- Analyze user journey, breadcrumbs, affected versions
- Link to codebase to find root cause

### mobiai-create-pr
- Verify tests pass BEFORE pushing
- Create well-structured PR with description, screenshots if UI
- Link to approved issue (issue-first enforcement)
- Do NOT push without user approval of final diff + test evidence

### mobiai-fix-issue
- Fetch issue from tracker (Jira, GitHub Issues, Linear)
- Understand, investigate root cause, apply fix with tests, verify
- Small fixes: autonomous end-to-end, gates only before push
- Complex changes: gate at each phase boundary
- MUST NOT push without user approving final diff + test evidence

### mobiai-flutter
- Flutter/Dart project patterns: state management, widget trees
- Build: `flutter build`, `flutter test`, `flutter analyze`
- Debugging: DevTools, hot reload, flutter logs
- Package management: `pubspec.yaml`, `flutter pub get`

### mobiai-graph
- Use `mobiai graph search/callers/context` instead of grep
- Pre-flight: check `.mobiai/graph/index.json` exists
- If not found, suggest `mobiai graph init` — do NOT run it
- Semantic code exploration for mobile codebases

### mobiai-ios-architecture
- iOS project structure: Xcode groups, Swift/ObjC patterns
- Follow MVVM, Coordinator, or existing patterns
- UIKit vs SwiftUI awareness
- Respect existing project organization

### mobiai-ios-build
- Build: `xcodebuild` with scheme/workspace configuration
- Dependencies: CocoaPods (Podfile) or SPM (Package.swift)
- Troubleshooting: build phases, provisioning, code signing
- SPM dependency resolution via Xcode

### mobiai-ios-device
- iOS Simulator control via `xcrun simctl`
- Boot, install, launch, capture screenshots, read logs
- Manage simulator devices and runtimes

### mobiai-ios-testing
- XCTest for unit and UI tests
- Test plans, code coverage with xccov
- Snapshot testing with snapshot libraries
- Run via Xcode or `xcodebuild test`

### mobiai-kmp
- KMP project structure: shared/commonMain + platform-specific source sets
- expect/actual declarations for platform-specific code
- Build targets: Android, iOS, JVM, JS, Wasm
- Dependencies in commonMain via Kotlin multiplatform libraries

### mobiai-mobile-brainstorming
- Explore user intent and requirements before ANY creative mobile work
- Understand project context first
- Ask questions one at a time to refine ideas
- Produce design and get user approval before implementation

### mobiai-mobile-debugging
- ALWAYS find root cause before attempting fixes
- Phased evidence gathering: collect data → form hypothesis → verify
- For small fixes with clear evidence: run autonomously
- For complex cases: gate at known-vs-assumed and root-cause steps

### mobiai-mobile-executing-plans
- Load written implementation plan
- Review critically for completeness/accuracy
- Execute all tasks in order
- Report completion with verification

### mobiai-mobile-executing-plans-with-subagents
- Fresh subagent per task from the plan
- Mandatory two-stage review after each task: spec compliance → code quality
- Do NOT hand-execute plan tasks in-session

### mobiai-mobile-finishing-branch
- Verify tests pass
- Present integration options to user
- Handle chosen workflow (merge, push, PR)
- Clean up temporary files

### mobiai-mobile-parallel-agents
- ONLY for independent, unrelated problems
- One subagent per problem with isolated context
- Each agent gets only what it needs, no shared history
- Collect and synthesize results

### mobiai-mobile-planning
- When task spans multiple steps/files/subsystems
- Produce written plan BEFORE touching any code
- User MUST approve plan before implementation
- Document files to touch, code needed, testing, verification

### mobiai-mobile-tdd
- HARD GATE: do NOT write production code before failing test exists
- RED → GREEN → REFACTOR cycle
- Tests at appropriate level (unit, integration, UI)
- No exceptions regardless of perceived simplicity

### mobiai-mobile-verification
- HARD GATE: do NOT claim completion without fresh verification
- Run tests, check outputs NOW (not from memory)
- Report actual verification results with evidence

### mobiai-mobile-worktrees
- Use git worktrees for isolated development branches
- Systematic directory selection with safety verification
- Each worktree is a separate working directory sharing the same repo
- Clean up worktrees when done

### mobiai-react-native
- React Native project patterns: navigation, state management
- Build: `npx react-native`, Metro bundler
- Debugging: Flipper, React DevTools, Hermes
- Package management: npm/yarn, package.json

### mobiai-reproduce-bug
- Interact with running app on device/emulator/simulator via UI automation
- Confirm bug exists, capture evidence (screenshots, logs)
- Document reproduction steps for the developer

### mobiai-review-code
- Mobile-specific code review: lifecycle, memory leaks, thread safety
- Platform-specific pitfalls (Android Activity/fragment, iOS VC)
- Static analysis can't catch these; human review required

### mobiai-update
- Update mobiai binary via official install script
- Detect platform, download from GitHub Releases
- Replace binary in `~/.mobiai/bin/`
- Confirm new version after update

### mobiai-write-tests
- Write tests following project's existing patterns
- Cover bug fix or new feature
- Unit + integration levels as appropriate
- Prevent regression with comprehensive test coverage

### mobiai-writing-skills
- Guide user through creating a new SKILL.md
- Proper frontmatter with name, description, trigger
- Actionable instructions, clear patterns
- License, author, version metadata

### migrate-xml-views-to-jetpack-compose
- Plan: identify XML layout, dependencies needed
- Migrate step by step: layout → state → theming
- Validation: visual comparison, behavior parity
- Clean up: remove XML layout and unused resources

### navigation-3
- Jetpack Navigation 3 setup: version catalog, dependencies
- Scenes: dialog, bottom sheet, list-detail, two-pane, supporting pane
- Deep links, conditional nav, results, multiple backstacks
- Integration with Hilt, ViewModel, Kotlin, view interop

### play-billing-library-version-upgrade
- Upgrade Play Billing Library from legacy to latest stable
- Check version catalog and dependencies
- Update billing client API calls (new API patterns)
- Test purchases and subscriptions after migration

### r8-analyzer
- Analyze build.gradle.kts proguardFiles and keep rules
- Identify redundant, broad package-wide rules
- Rules that subsume library consumer keep rules
- Optimize app size by removing unnecessary rules

### using-mobiai
- Dispatched as subagent: SKIP this skill
- Otherwise: invoke `Skill` tool if >= 1% chance a skill applies
- If a skill applies, you MUST use it — no choice
- Non-negotiable: Skill tool required before ANY response
