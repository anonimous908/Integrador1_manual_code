$path1 = "shared\src\commonMain\kotlin\org\example\project\presentation\CreateAccountScreen.kt"
$content1 = Get-Content $path1 -Raw
$content1 = $content1 -replace "DevSpaceRegisterColors\.Background", "DevSpaceColors.background"
$content1 = $content1 -replace "DevSpaceRegisterColors\.SurfaceContainerHigh", "DevSpaceColors.surfaceContainerHigh"
$content1 = $content1 -replace "DevSpaceRegisterColors\.SurfaceContainer", "DevSpaceColors.surfaceContainer"
$content1 = $content1 -replace "DevSpaceRegisterColors\.SurfaceVariant", "DevSpaceColors.surfaceContainerHigh"
$content1 = $content1 -replace "DevSpaceRegisterColors\.PrimaryGlow", "DevSpaceColors.PrimaryGlow"
$content1 = $content1 -replace "DevSpaceRegisterColors\.Primary", "DevSpaceColors.primary"
$content1 = $content1 -replace "DevSpaceRegisterColors\.OnPrimary", "DevSpaceColors.onPrimaryContainer"
$content1 = $content1 -replace "DevSpaceRegisterColors\.OnBackground", "DevSpaceColors.onSurface"
$content1 = $content1 -replace "DevSpaceRegisterColors\.OnSurfaceVariant", "DevSpaceColors.onSurfaceVariant"
$content1 = $content1 -replace "DevSpaceRegisterColors\.OnSurface", "DevSpaceColors.onSurface"
$content1 = $content1 -replace "DevSpaceRegisterColors\.OutlineVariant", "DevSpaceColors.outlineVariant"
$content1 = $content1 -replace "DevSpaceRegisterColors\.Outline", "DevSpaceColors.outline"
$content1 = $content1 -replace "DevSpaceRegisterColors\.InputBackground", "DevSpaceColors.InputBackground"
$content1 = $content1 -replace "DevSpaceRegisterColors\.GlassPanel", "DevSpaceColors.GlassPanel"
$content1 = $content1 -replace "(?sm)private object DevSpaceRegisterColors \{.*?\n\}", ""
$content1 = $content1 -replace "import androidx\.compose\.ui\.graphics\.Color", "import androidx.compose.ui.graphics.Color`r`nimport org.example.project.presentation.theme.DevSpaceColors"
Set-Content -Path $path1 -Value $content1

$path2 = "shared\src\commonMain\kotlin\org\example\project\presentation\HomeScreen.kt"
$content2 = Get-Content $path2 -Raw
$content2 = $content2 -replace "Color\(0xFF131313\)", "DevSpaceColors.background"
$content2 = $content2 -replace "Color\(0xFF201F1F\)", "DevSpaceColors.surfaceContainer"
$content2 = $content2 -replace "Color\(0xFFE5E2E1\)", "DevSpaceColors.onSurface"
$content2 = $content2 -replace "Color\(0xFFBEC7D4\)", "DevSpaceColors.onSurfaceVariant"
$content2 = $content2 -replace "Icons\.Filled\.ExitToApp", "Icons.AutoMirrored.Filled.ExitToApp"
$content2 = $content2 -replace "import androidx\.compose\.ui\.graphics\.Color", "import androidx.compose.ui.graphics.Color`r`nimport org.example.project.presentation.theme.DevSpaceColors"
Set-Content -Path $path2 -Value $content2

$path3 = "shared\src\commonMain\kotlin\org\example\project\presentation\PrivacyPolicyScreen.kt"
$content3 = Get-Content $path3 -Raw
$content3 = $content3 -replace "Color\(0xFF131313\)", "DevSpaceColors.background"
$content3 = $content3 -replace "Color\(0xFFE5E2E1\)", "DevSpaceColors.onSurface"
$content3 = $content3 -replace "Color\(0xFFBEC7D4\)", "DevSpaceColors.onSurfaceVariant"
$content3 = $content3 -replace "Color\(0xFF98CBFF\)", "DevSpaceColors.primary"
$content3 = $content3 -replace "Icons\.Filled\.ArrowBack", "Icons.AutoMirrored.Filled.ArrowBack"
$content3 = $content3 -replace "import androidx\.compose\.ui\.graphics\.Color", "import androidx.compose.ui.graphics.Color`r`nimport org.example.project.presentation.theme.DevSpaceColors"
Set-Content -Path $path3 -Value $content3

$path4 = "shared\src\commonMain\kotlin\org\example\project\presentation\TermsAndConditionsScreen.kt"
$content4 = Get-Content $path4 -Raw
$content4 = $content4 -replace "Color\(0xFF131313\)", "DevSpaceColors.background"
$content4 = $content4 -replace "Color\(0xFFE5E2E1\)", "DevSpaceColors.onSurface"
$content4 = $content4 -replace "Color\(0xFFBEC7D4\)", "DevSpaceColors.onSurfaceVariant"
$content4 = $content4 -replace "Color\(0xFF98CBFF\)", "DevSpaceColors.primary"
$content4 = $content4 -replace "Icons\.Filled\.ArrowBack", "Icons.AutoMirrored.Filled.ArrowBack"
$content4 = $content4 -replace "import androidx\.compose\.ui\.graphics\.Color", "import androidx.compose.ui.graphics.Color`r`nimport org.example.project.presentation.theme.DevSpaceColors"
Set-Content -Path $path4 -Value $content4
