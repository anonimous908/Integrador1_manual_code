package org.example.project.domain.service

object LanguageDetector {

    private val extensionMap = mapOf(
        "kt" to "Kotlin", "kts" to "Kotlin",
        "java" to "Java",
        "py" to "Python", "pyw" to "Python",
        "js" to "JavaScript", "mjs" to "JavaScript", "cjs" to "JavaScript",
        "ts" to "TypeScript", "tsx" to "TypeScript",
        "c" to "C", "h" to "C",
        "cpp" to "C++", "cc" to "C++", "cxx" to "C++", "hpp" to "C++", "hxx" to "C++",
        "cs" to "C#",
        "rs" to "Rust",
        "go" to "Go",
        "swift" to "Swift",
        "rb" to "Ruby",
        "php" to "PHP",
        "dart" to "Dart",
        "scala" to "Scala", "sc" to "Scala",
        "r" to "R",
        "asm" to "Assembly", "s" to "Assembly",
        "sh" to "Shell/Bash", "bash" to "Shell/Bash", "zsh" to "Shell/Bash",
        "ps1" to "PowerShell", "psm1" to "PowerShell",
        "sql" to "SQL",
        "html" to "HTML", "htm" to "HTML",
        "css" to "CSS", "scss" to "SCSS", "less" to "LESS",
        "xml" to "XML", "svg" to "XML",
        "json" to "JSON",
        "yaml" to "YAML", "yml" to "YAML",
        "dockerfile" to "Dockerfile",
        "makefile" to "Makefile",
        "cmake" to "CMake", "cmakelists" to "CMake",
        "ll" to "LLVM IR",
        "wat" to "WebAssembly", "wasm" to "WebAssembly",
        "vhd" to "VHDL", "vhdl" to "VHDL",
        "v" to "Verilog",
        "f" to "Fortran", "f90" to "Fortran", "f95" to "Fortran",
        "cob" to "COBOL", "cbl" to "COBOL",
        "lua" to "Lua",
        "pl" to "Perl", "pm" to "Perl",
        "hs" to "Haskell",
        "ex" to "Elixir", "exs" to "Elixir",
        "erl" to "Erlang",
        "clj" to "Clojure",
        "groovy" to "Groovy",
        "mm" to "Objective-C",
        "zig" to "Zig",
        "nim" to "Nim",
        "jl" to "Julia",
        "toml" to "TOML",
        "ini" to "INI",
        "gradle" to "Gradle", "gradle.kts" to "Gradle",
        "md" to "Markdown", "mdx" to "Markdown",
        "tf" to "Terraform", "tfvars" to "Terraform",
        "proto" to "Protobuf"
    )

    private val languageToExtension = mapOf(
        "Kotlin" to "kt", "Java" to "java", "Python" to "py",
        "JavaScript" to "js", "TypeScript" to "ts", "C" to "c", "C++" to "cpp",
        "C#" to "cs", "Rust" to "rs", "Go" to "go", "Swift" to "swift",
        "Ruby" to "rb", "PHP" to "php", "Dart" to "dart", "Scala" to "scala",
        "R" to "r", "Assembly" to "asm", "Shell/Bash" to "sh",
        "PowerShell" to "ps1", "SQL (MySQL)" to "sql", "SQL" to "sql",
        "MongoDB" to "js", "HTML" to "html",
        "CSS" to "css", "JSON" to "json", "YAML" to "yml", "XML" to "xml",
        "Dockerfile" to "Dockerfile", "Makefile" to "Makefile",
        "CMake" to "cmake", "LLVM IR" to "ll", "WebAssembly" to "wat",
        "VHDL" to "vhd", "Verilog" to "v", "MATLAB" to "m",
        "Fortran" to "f90", "COBOL" to "cbl", "Lua" to "lua",
        "Perl" to "pl", "Haskell" to "hs", "Elixir" to "ex",
        "Erlang" to "erl", "Clojure" to "clj", "Groovy" to "groovy",
        "Objective-C" to "m", "Zig" to "zig", "Nim" to "nim", "Julia" to "jl",
        "Markdown" to "md", "TOML" to "toml", "INI" to "ini",
        "Terraform" to "tf", "SCSS" to "scss", "LESS" to "less",
        "Plain Text" to "txt", "Protobuf" to "proto"
    )

    fun detectFromFileName(fileName: String): String? {
        if (fileName.isBlank()) return null
        val name = fileName.lowercase().trim()
        if (name == "dockerfile") return "Dockerfile"
        if (name == "makefile") return "Makefile"
        val ext = name.substringAfterLast('.', "")
        if (ext.isEmpty() || ext == name) return null
        return extensionMap[ext]
    }

    fun getDefaultExtension(language: String): String {
        return languageToExtension[language] ?: "txt"
    }

    fun detectFromCode(code: String): String? {
        if (code.isBlank()) return null
        val lines = code.lines().map { it.trim() }.filter { it.isNotBlank() }
        if (lines.isEmpty()) return null

        val firstLine = lines.first()
        if (firstLine.startsWith("#!")) {
            return detectFromShebang(firstLine)
        }

        val fullCode = lines.joinToString("\n")
        val scores = mutableMapOf<String, Int>()

        for (rule in languageRules) {
            var score = 0

            for (marker in rule.uniqueMarkers) {
                if (fullCode.contains(marker)) score += 100
            }

            for (pattern in rule.strongPatterns) {
                if (pattern.containsMatchIn(fullCode)) score += 30
            }

            for (keyword in rule.keywords) {
                if (fullCode.contains(keyword)) score += 10
            }

            for (anti in rule.antiPatterns) {
                if (fullCode.contains(anti)) score -= 20
            }

            if (score >= rule.minConfidence) {
                scores[rule.language] = score
            }
        }

        // Also check for plain text by analyzing character distribution
        if (isPlainText(fullCode)) {
            scores["Plain Text"] = maxOf(scores["Plain Text"] ?: 0, 50)
        }

        if (scores.isEmpty()) return null

        val best = scores.maxByOrNull { it.value } ?: return null
        return if (best.value >= 50) best.key else null
    }

    private fun isPlainText(code: String): Boolean {
        if (code.length < 20) return false
        val specialChars = code.count { c -> c in "{}()[]<>;:=+-*/|&!@#$%^`~\\\"'.," }
        val ratio = specialChars.toDouble() / code.length.toDouble()
        // Code typically has >5% special characters. Plain text has <3%.
        return ratio < 0.03
    }

    private fun detectFromShebang(shebang: String): String? {
        val s = shebang.lowercase()
        return when {
            s.contains("python") -> "Python"
            s.contains("node") || s.contains("javascript") -> "JavaScript"
            s.contains("bash") || s.contains("sh") -> "Shell/Bash"
            s.contains("pwsh") || s.contains("powershell") -> "PowerShell"
            s.contains("ruby") -> "Ruby"
            s.contains("perl") -> "Perl"
            s.contains("php") -> "PHP"
            s.contains("lua") -> "Lua"
            s.contains("mongo") -> "MongoDB"
            else -> null
        }
    }

    // ─── LANGUAGE RULES ───────────────────────────────────────────────────

    private data class LanguageRule(
        val language: String,
        val uniqueMarkers: List<String>,
        val strongPatterns: List<Regex>,
        val keywords: List<String>,
        val antiPatterns: List<String>,
        val minConfidence: Int = 60
    )

    private val languageRules = listOf(
        LanguageRule(
            language = "SQL (MySQL)",
            uniqueMarkers = listOf("`", "AUTO_INCREMENT", "ENGINE=", "TINYINT", "MEDIUMINT", "BIGINT", "ENUM("),
            strongPatterns = listOf(
                Regex("SELECT\\s+.*\\s+FROM\\s+", RegexOption.IGNORE_CASE),
                Regex("INSERT\\s+INTO\\s+`?\\w+`?", RegexOption.IGNORE_CASE),
                Regex("CREATE\\s+(TEMPORARY\\s+)?TABLE\\s+", RegexOption.IGNORE_CASE),
                Regex("ALTER\\s+TABLE\\s+", RegexOption.IGNORE_CASE),
                Regex("DROP\\s+(TABLE|DATABASE|INDEX)\\s+", RegexOption.IGNORE_CASE),
                Regex("JOIN\\s+.*\\s+ON\\s+", RegexOption.IGNORE_CASE),
                Regex("GROUP\\s+BY\\s+", RegexOption.IGNORE_CASE),
                Regex("ORDER\\s+BY\\s+", RegexOption.IGNORE_CASE),
                Regex("HAVING\\s+", RegexOption.IGNORE_CASE),
                Regex("PRIMARY\\s+KEY\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("FOREIGN\\s+KEY\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("AUTO_INCREMENT", RegexOption.IGNORE_CASE),
                Regex("LIMIT\\s+\\d+", RegexOption.IGNORE_CASE),
                Regex("LEFT\\s+(OUTER\\s+)?JOIN", RegexOption.IGNORE_CASE),
                Regex("UNION\\s+(ALL\\s+)?SELECT", RegexOption.IGNORE_CASE),
                Regex("EXPLAIN\\s+SELECT", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf(
                "SELECT", "FROM", "WHERE", "INSERT", "UPDATE", "DELETE", "CREATE TABLE",
                "VARCHAR", "INTEGER", "BOOLEAN", "DATETIME", "TIMESTAMP", "TEXT", "BLOB",
                "INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "CROSS JOIN", "FULL OUTER JOIN",
                "COUNT(", "SUM(", "AVG(", "MAX(", "MIN(", "DISTINCT", "AS ",
                "NOT NULL", "DEFAULT ", "UNIQUE", "INDEX", "VIEW",
                "BEGIN", "COMMIT", "ROLLBACK", "TRANSACTION", "SAVEPOINT",
                "CASCADE", "RESTRICT", "SET NULL", "NO ACTION",
                "TRIGGER", "PROCEDURE", "FUNCTION", "CURSOR", "DECLARE",
                "VACUUM", "REINDEX", "EXPLAIN", "ANALYZE",
                "CHAR(", "DECIMAL(", "NUMERIC(", "FLOAT(", "DOUBLE PRECISION",
                "SERIAL", "IDENTITY", "GENERATED ALWAYS", "GENERATED BY DEFAULT"
            ),
            antiPatterns = listOf(
                "<!DOCTYPE", "<html", "function (", "var ",
                "package ", "import {", "console.log", "System.out",
                "func ", "def ", "class ", "public class"
            ),
            minConfidence = 40
        ),
        LanguageRule(
            language = "SQL",
            uniqueMarkers = emptyList(),
            strongPatterns = listOf(
                Regex("SELECT\\s+.*\\s+FROM\\s+", RegexOption.IGNORE_CASE),
                Regex("INSERT\\s+INTO\\s+\\w+", RegexOption.IGNORE_CASE),
                Regex("CREATE\\s+TABLE\\s+", RegexOption.IGNORE_CASE),
                Regex("JOIN\\s+.*\\s+ON\\s+", RegexOption.IGNORE_CASE),
                Regex("GROUP\\s+BY\\s+", RegexOption.IGNORE_CASE),
                Regex("ORDER\\s+BY\\s+", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("SELECT", "FROM", "WHERE", "INSERT", "UPDATE", "DELETE", "CREATE TABLE", "VARCHAR"),
            antiPatterns = listOf("<!DOCTYPE", "<html", "function (", "var ", "System.out"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "MongoDB",
            uniqueMarkers = listOf("ObjectId(", "ISODate(", "NumberInt(", "NumberLong(", "NumberDecimal(", "BinData("),
            strongPatterns = listOf(
                Regex("db\\.\\w+\\.find\\(", RegexOption.IGNORE_CASE),
                Regex("db\\.\\w+\\.aggregate\\(", RegexOption.IGNORE_CASE),
                Regex("db\\.\\w+\\.insert(One|Many)?\\(", RegexOption.IGNORE_CASE),
                Regex("db\\.\\w+\\.update(One|Many)?\\(", RegexOption.IGNORE_CASE),
                Regex("db\\.\\w+\\.delete(One|Many)?\\(", RegexOption.IGNORE_CASE),
                Regex("db\\.\\w+\\.count\\(", RegexOption.IGNORE_CASE),
                Regex("db\\.\\w+\\.distinct\\(", RegexOption.IGNORE_CASE),
                Regex("db\\.\\w+\\.createIndex\\(", RegexOption.IGNORE_CASE),
                Regex("db\\.\\w+\\.drop\\(", RegexOption.IGNORE_CASE),
                Regex("db\\.\\w+\\.mapReduce\\(", RegexOption.IGNORE_CASE),
                Regex("\"\\$[a-zA-Z]+\"\\s*:", RegexOption.IGNORE_CASE),
                Regex("\\{\\s*\"?\\$[a-zA-Z]+\"?\\s*:", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf(
                "\$match", "\$group", "\$sort", "\$project", "\$limit", "\$skip",
                "\$unwind", "\$lookup", "\$addFields", "\$set", "\$unset",
                "\$push", "\$sum", "\$avg", "\$first", "\$last", "\$max", "\$min",
                "\$eq", "\$gt", "\$gte", "\$lt", "\$lte", "\$ne", "\$in", "\$nin",
                "\$and", "\$or", "\$not", "\$nor", "\$exists", "\$type",
                "\$regex", "\$text", "\$search", "\$elemMatch", "\$all", "\$size",
                "\$inc", "\$mul", "\$rename", "\$setOnInsert", "\$currentDate",
                "\$addToSet", "\$pop", "\$pull", "\$pullAll",
                "ObjectId", "ISODate", "NumberInt", "NumberLong",
                "db.getCollection(", "db.createCollection(", "show dbs", "show collections",
                "mongosh", "mongo ", "use ", "rs.status()", "rs.initiate("
            ),
            antiPatterns = listOf(
                "SELECT ", "INSERT INTO", "CREATE TABLE", "DROP TABLE",
                "public class", "def ", "function (", "<!DOCTYPE"
            ),
            minConfidence = 50
        ),
        LanguageRule(
            language = "C#",
            uniqueMarkers = listOf(
                "using System;", "namespace ", "Console.WriteLine",
                "Console.ReadLine", "string[] args", "String[] args"
            ),
            strongPatterns = listOf(
                Regex("using\\s+System\\.", RegexOption.IGNORE_CASE),
                Regex("namespace\\s+\\w+", RegexOption.IGNORE_CASE),
                Regex("Console\\.\\w+\\(", RegexOption.IGNORE_CASE),
                Regex("IEnumerable<|IQueryable<|ICollection<|IDictionary<|IList<|ISet<"),
                Regex("async\\s+Task|async\\s+Task<"),
                Regex("\\[HttpGet\\]|\\[HttpPost\\]|\\[Route\\(|\\[ApiController\\]|\\[FromBody\\]|\\[FromQuery\\]"),
                Regex("\\[TestMethod\\]|\\[TestClass\\]|\\[TestFixture\\]"),
                Regex("class\\s+\\w+\\s*:\\s*\\w+", RegexOption.IGNORE_CASE),
                Regex("public\\s+(class|interface|enum|struct|record)", RegexOption.IGNORE_CASE),
                Regex("var\\s+\\w+\\s*=\\s*new\\s+"),
                Regex("=>\\s*\""),
                Regex("string\\.(Join|Format|Concat|Empty|IsNullOr)", RegexOption.IGNORE_CASE),
                Regex("List<|Dictionary<|HashSet<|ConcurrentDictionary<|ConcurrentBag<|ObservableCollection<"),
                Regex("\\$\\{.*\\}"),
                Regex("\"[^\"]*\\{[0-9]+}[^\"]*\""),
                Regex("delegate\\s+"),
                Regex("event\\s+EventHandler"),
                Regex("nameof\\(|typeof\\(|sizeof\\("),
                Regex("params\\s+\\w+\\[\\]"),
                Regex("yield\\s+(return|break)")
            ),
            keywords = listOf(
                "using", "namespace", "class", "struct", "interface", "enum", "record",
                "public", "private", "protected", "internal", "static", "readonly", "const",
                "virtual", "override", "abstract", "sealed", "async", "await", "var",
                "new", "return", "if", "else", "for", "foreach", "while", "do",
                "switch", "case", "break", "continue", "try", "catch", "finally",
                "throw", "null", "true", "false", "this", "base",
                "object", "string", "int", "long", "double", "float", "bool", "decimal",
                "char", "byte", "short", "uint", "ulong", "ushort", "sbyte",
                "Task", "Task<>", "ValueTask", "async", "await",
                "get;", "set;", "init;", "{ get;", "{ set;",
                "IEnumerable", "IAsyncEnumerable", "IQueryable",
                "DateTime", "TimeSpan", "Guid", "Uri",
                "LINQ", "Entity", "DbContext", "DbSet", "Migrations",
                "AddScoped", "AddTransient", "AddSingleton", "Configure",
                "await using", "lock (", "typeof(", "nameof(",
                "Tuple", "ValueTuple", "Nullable", "Exception", "EventArgs",
                "Invoke(", "BeginInvoke(", "EndInvoke",
                "StringBuilder", "StringComparer", "CultureInfo",
                "XDocument", "XElement", "XAttribute", "XmlDocument", "XmlNode",
                "DataSet", "DataTable", "DataRow", "SqlConnection", "SqlCommand",
                "HttpClient", "HttpResponseMessage", "HttpRequestMessage",
                "JsonSerializer", "JsonConvert", "JObject", "JArray", "JToken"
            ),
            antiPatterns = listOf(
                "import java.", "println(", "func ", "package main",
                "def ", "console.log", "SELECT ", "FROM ",
                "use std::", "#include", "#!/"
            ),
            minConfidence = 50
        ),
        LanguageRule(
            language = "Kotlin",
            uniqueMarkers = listOf("fun ", "val ", "var ", "companion object", "data class ", "sealed class ", "sealed interface "),
            strongPatterns = listOf(
                Regex("fun\\s+\\w+\\s*\\(.*\\)\\s*[:{]?", RegexOption.IGNORE_CASE),
                Regex("(val|var)\\s+\\w+\\s*[=:]", RegexOption.IGNORE_CASE),
                Regex("(\\?\\.|!!|\\.let\\s*\\{|\\.apply\\s*\\{|\\.also\\s*\\{|\\.run\\s*\\{)"),
                Regex("@Composable"),
                Regex("remember\\s*\\{"),
                Regex("mutableStateOf\\(|mutableStateListOf\\(|mutableStateMapOf\\("),
                Regex("LaunchedEffect\\(|DisposableEffect\\(|SideEffect\\s*\\{")
            ),
            keywords = listOf("fun", "val", "var", "class", "object", "interface", "sealed", "data", "enum", "when", "if", "else", "for", "while", "do", "return", "break", "continue", "in", "is", "as", "null", "true", "false", "import", "package", "throw", "try", "catch", "finally", "suspend", "this", "super", "typealias", "companion", "internal", "private", "protected", "public", "override", "abstract", "open", "final", "const", "lateinit", "by"),
            antiPatterns = listOf("#include", "public static void main", "console.log"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "Java",
            uniqueMarkers = listOf("public static void main", "System.out.println", "System.out.print"),
            strongPatterns = listOf(
                Regex("public\\s+class\\s+\\w+\\s*(extends|implements|\\{)", RegexOption.IGNORE_CASE),
                Regex("public\\s+static\\s+void\\s+main\\s*\\(String", RegexOption.IGNORE_CASE),
                Regex("@Override"),
                Regex("@Autowired|@Component|@Service|@Repository|@Controller|@RestController"),
                Regex("@Entity|@Table|@Column|@Id|@GeneratedValue|@ManyToOne|@OneToMany"),
                Regex("@Bean|@Configuration|@SpringBootApplication"),
                Regex("private\\s+static\\s+final\\s+\\w+\\s+\\w+\\s*=")
            ),
            keywords = listOf("public", "private", "protected", "static", "final", "abstract", "class", "interface", "enum", "extends", "implements", "new", "return", "if", "else", "for", "while", "do", "switch", "case", "break", "continue", "throw", "throws", "try", "catch", "finally", "import", "package", "this", "super", "null", "true", "false", "void", "int", "String", "boolean", "long", "double", "float", "byte", "char", "short"),
            antiPatterns = listOf("#include", "func ", "val ", "def ", "console.log"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "Python",
            uniqueMarkers = emptyList(),
            strongPatterns = listOf(
                Regex("def\\s+\\w+\\s*\\(.*\\)\\s*:", RegexOption.IGNORE_CASE),
                Regex("class\\s+\\w+\\s*\\(.*\\)\\s*:", RegexOption.IGNORE_CASE),
                Regex("import\\s+\\w+(\\s+as\\s+\\w+)?$", RegexOption.MULTILINE),
                Regex("from\\s+\\w+\\s+import\\s+", RegexOption.IGNORE_CASE),
                Regex("if\\s+__name__\\s*==\\s*\"__main__\"", RegexOption.IGNORE_CASE),
                Regex("self\\.\\w+"),
                Regex("print\\(", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("def", "class", "if", "elif", "else", "for", "while", "import", "from", "as", "return", "yield", "raise", "try", "except", "finally", "with", "lambda", "pass", "break", "continue", "and", "or", "not", "is", "in", "True", "False", "None", "self", "global", "nonlocal", "assert", "del", "async", "await"),
            antiPatterns = listOf("public class", "SELECT ", "console.log", "#include", "using System"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "JavaScript",
            uniqueMarkers = emptyList(),
            strongPatterns = listOf(
                Regex("console\\.(log|error|warn|info)\\(", RegexOption.IGNORE_CASE),
                Regex("const\\s+\\w+\\s*=\\s*require\\(", RegexOption.IGNORE_CASE),
                Regex("module\\.exports\\s*=", RegexOption.IGNORE_CASE),
                Regex("(const|let|var)\\s+\\w+\\s*=\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("=>\\s*\\{", RegexOption.IGNORE_CASE),
                Regex("function\\s+\\w+\\s*\\(.*\\)\\s*\\{", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("function", "const", "let", "var", "class", "extends", "new", "return", "if", "else", "for", "while", "do", "switch", "case", "break", "continue", "try", "catch", "finally", "throw", "import", "export", "default", "from", "as", "async", "await", "yield", "this", "super", "null", "undefined", "true", "false", "typeof", "instanceof"),
            antiPatterns = listOf("#include", "public static void main", "SELECT FROM", "def ", ": Int", ": String"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "TypeScript",
            uniqueMarkers = listOf(": string", ": number", ": boolean", ": void", ": any", ": never"),
            strongPatterns = listOf(
                Regex(":\\s*(string|number|boolean|void|any|never|unknown|Record|Partial|Required|Pick|Omit)\\b"),
                Regex("interface\\s+\\w+\\s*\\{", RegexOption.IGNORE_CASE),
                Regex("type\\s+\\w+\\s*=", RegexOption.IGNORE_CASE),
                Regex("enum\\s+\\w+\\s*\\{", RegexOption.IGNORE_CASE),
                Regex("React\\.(FC|FunctionComponent|useState|useEffect|useCallback|useMemo)"),
                Regex("useState<|useRef<")
            ),
            keywords = listOf("interface", "type", "enum", "readonly", "as const", "implements", "abstract", "declare", "namespace"),
            antiPatterns = listOf("#include", "public static void main", "SELECT FROM"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "C++",
            uniqueMarkers = listOf("#include", "std::", "nullptr", "template<", "int main"),
            strongPatterns = listOf(
                Regex("#include\\s*[<\"]", RegexOption.IGNORE_CASE),
                Regex("std::\\w+", RegexOption.IGNORE_CASE),
                Regex("cout\\s*<<|cin\\s*>>"),
                Regex("int\\s+main\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("template\\s*<"),
                Regex("vector<|map<|set<|unordered_"),
                Regex("unique_ptr<|shared_ptr<|weak_ptr<|make_unique<|make_shared<")
            ),
            keywords = listOf("auto", "break", "case", "const", "continue", "default", "do", "else", "enum", "extern", "for", "goto", "if", "register", "return", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while", "class", "namespace", "using", "typename", "public", "private", "protected", "virtual", "override", "new", "delete", "this", "nullptr", "true", "false"),
            antiPatterns = listOf("public class", "import java", "from ", "def ", "SELECT FROM"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "Shell/Bash",
            uniqueMarkers = listOf("#!/bin/bash", "#!/bin/sh", "#!/usr/bin/env bash"),
            strongPatterns = listOf(
                Regex("^#!\\s*/.*(bash|sh|zsh|fish)", RegexOption.IGNORE_CASE),
                Regex("^echo\\s+", RegexOption.IGNORE_CASE),
                Regex("\\bif\\s+\\[\\s+", RegexOption.IGNORE_CASE),
                Regex("^\\s*fi\\s*$", RegexOption.MULTILINE),
                Regex("\\$\\{|\\$\\(|\"\\$\\w+\"|'\\$\\w+'"),
                Regex("chmod\\s+[0-7]+"),
                Regex("\\|\\s*(grep|awk|sed|sort|uniq|cut|head|tail|wc|xargs|tee)\\b"),
                Regex("^#\\s*TODO|^#\\s*FIXME|^#\\s*NOTE"),
                Regex("apt-get|apt\\s+install|yum\\s+install|brew\\s+install|pacman"),
                Regex("^source\\s+|^\\.\\s+/")
            ),
            keywords = listOf("echo", "export", "if", "then", "else", "elif", "fi", "for", "while", "do", "done", "case", "esac", "function", "local", "return", "exit", "read", "declare", "alias", "unalias", "sudo", "chmod", "chown", "grep", "awk", "sed", "find", "tar", "gzip", "gunzip", "wget", "curl", "cd", "ls", "cp", "mv", "rm", "mkdir", "rmdir", "touch", "cat", "trap", "set -", "shift"),
            antiPatterns = listOf("public class", "def ", "func ", "SELECT FROM", "import java"),
            minConfidence = 30
        ),
        LanguageRule(
            language = "PowerShell",
            uniqueMarkers = listOf("Write-Host", "Get-ChildItem", "Set-ExecutionPolicy"),
            strongPatterns = listOf(
                Regex("Write-(Host|Output|Error|Warning|Verbose|Debug|Progress)", RegexOption.IGNORE_CASE),
                Regex("Get-(ChildItem|Content|Process|Service|EventLog|WmiObject|Item|Date|Location)", RegexOption.IGNORE_CASE),
                Regex("New-(Item|Object|Module|Alias)", RegexOption.IGNORE_CASE),
                Regex("ForEach-Object|Where-Object|Select-Object|Sort-Object|Group-Object"),
                Regex("\\$\\w+\\s*=\\s*", RegexOption.IGNORE_CASE),
                Regex("param\\s*\\(", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("\$env:", "\$global:", "\$script:", "\$local:", "\$private:", "\$using:", "\$PSVersionTable", "\$profile", "\$PWD", "\$HOME", "Invoke-", "Start-", "Stop-", "Restart-", "Test-", "Wait-", "Out-", "Export-", "Import-", "ConvertTo-", "ConvertFrom-", "Format-", "Measure-"),
            antiPatterns = listOf("public class", "def ", "func ", "SELECT FROM"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "Rust",
            uniqueMarkers = listOf("fn main()", "let mut", "println!", "vec!"),
            strongPatterns = listOf(
                Regex("fn\\s+\\w+\\s*\\(.*\\)\\s*(->|\\{)", RegexOption.IGNORE_CASE),
                Regex("let\\s+mut\\s+", RegexOption.IGNORE_CASE),
                Regex("use\\s+std::", RegexOption.IGNORE_CASE),
                Regex("(println|print|eprintln|format|panic)!\\("),
                Regex("match\\s+\\w+\\s*\\{", RegexOption.IGNORE_CASE),
                Regex("impl\\s+\\w+\\s+for\\s+", RegexOption.IGNORE_CASE),
                Regex("struct\\s+\\w+\\s*\\{", RegexOption.IGNORE_CASE),
                Regex("enum\\s+\\w+\\s*\\{", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("fn", "let", "mut", "const", "static", "struct", "enum", "trait", "impl", "mod", "use", "pub", "crate", "self", "super", "match", "if", "else", "for", "while", "loop", "in", "break", "continue", "return", "where", "as", "ref", "move", "unsafe", "extern", "type", "dyn", "true", "false", "Some", "None", "Ok", "Err"),
            antiPatterns = listOf("public class", "#include", "console.log", "SELECT FROM"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "Go",
            uniqueMarkers = listOf("package main", "func main()", "fmt.Println", "go func"),
            strongPatterns = listOf(
                Regex("package\\s+(main|\\w+)", RegexOption.IGNORE_CASE),
                Regex("func\\s+\\w+\\s*\\(.*\\)\\s*(\\w+|\\{|\\()", RegexOption.IGNORE_CASE),
                Regex("fmt\\.(Print|Println|Printf|Sprintf|Errorf|Scan|Scanln|Fprintf)", RegexOption.IGNORE_CASE),
                Regex(":= ", RegexOption.IGNORE_CASE),
                Regex("defer\\s+", RegexOption.IGNORE_CASE),
                Regex("go\\s+func", RegexOption.IGNORE_CASE),
                Regex("chan\\s+|<-\\s*chan|make\\(chan", RegexOption.IGNORE_CASE),
                Regex("var\\s+\\w+\\s+(int|string|bool|float|byte|rune|error)", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("package", "import", "func", "var", "const", "type", "struct", "interface", "map", "chan", "return", "if", "else", "for", "range", "switch", "case", "break", "continue", "go", "defer", "select", "goto", "fallthrough", "nil", "true", "false", "make", "new", "append", "len", "cap", "panic", "recover", "close", "copy", "delete"),
            antiPatterns = listOf("public class", "#include", "console.log", "def ", "import java"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "Ruby",
            uniqueMarkers = listOf("def ", "end", "puts ", "require ", "@"),
            strongPatterns = listOf(
                Regex("def\\s+\\w+", RegexOption.IGNORE_CASE),
                Regex("\\bend\\s*$", RegexOption.MULTILINE),
                Regex("require\\s+['\"]", RegexOption.IGNORE_CASE),
                Regex("attr_(accessor|reader|writer)\\s+:", RegexOption.IGNORE_CASE),
                Regex("do\\s+\\|\\w+\\|", RegexOption.IGNORE_CASE),
                Regex("@\\w+\\s*=", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("def", "class", "module", "require", "include", "extend", "attr_accessor", "attr_reader", "attr_writer", "initialize", "self", "super", "yield", "raise", "rescue", "ensure", "begin", "unless", "case", "when", "proc", "lambda", "private", "protected", "public", "nil", "true", "false"),
            antiPatterns = listOf("public class", "#include", "console.log", "SELECT FROM", "func "),
            minConfidence = 40
        ),
        LanguageRule(
            language = "PHP",
            uniqueMarkers = listOf("<?php", "\$", "->", "::"),
            strongPatterns = listOf(
                Regex("<\\?php", RegexOption.IGNORE_CASE),
                Regex("\\$\\w+\\s*=", RegexOption.IGNORE_CASE),
                Regex("->\\w+\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("::", RegexOption.IGNORE_CASE),
                Regex("function\\s+\\w+\\s*\\(.*\\)\\s*\\{", RegexOption.IGNORE_CASE),
                Regex("namespace\\s+\\w+", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("echo", "print_r", "var_dump", "isset", "empty", "die", "array", "require_once", "include_once", "namespace", "use", "class", "extends", "implements", "public", "private", "protected", "static", "function", "return", "if", "else", "foreach", "for", "while", "try", "catch", "throw", "new", "this", "self", "parent"),
            antiPatterns = listOf("public static void main", "#include", "console.log"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "Swift",
            uniqueMarkers = listOf("import SwiftUI", "import Foundation", "import UIKit", "var body: some View"),
            strongPatterns = listOf(
                Regex("import\\s+(SwiftUI|Foundation|UIKit|AppKit|Combine|SwiftData)", RegexOption.IGNORE_CASE),
                Regex("(struct|class)\\s+\\w+\\s*:\\s*(View|ObservableObject|ViewModel)", RegexOption.IGNORE_CASE),
                Regex("@(State|Binding|ObservedObject|EnvironmentObject|StateObject|Published|Environment|AppStorage|SceneStorage|FocusState|GestureState)\\b"),
                Regex("guard\\s+let\\s+", RegexOption.IGNORE_CASE),
                Regex("if\\s+let\\s+\\w+\\s*=", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("import", "var", "let", "func", "struct", "class", "enum", "protocol", "extension", "override", "mutating", "convenience", "required", "init", "deinit", "guard", "if", "else", "for", "while", "repeat", "switch", "case", "break", "continue", "return", "throw", "throws", "try", "catch", "do", "where", "as", "is", "in", "self", "super", "nil", "true", "false"),
            antiPatterns = listOf("public static void main", "#include", "SELECT FROM"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "C",
            uniqueMarkers = listOf("#include <", "int main(", "printf(", "scanf(", "malloc(", "size_t"),
            strongPatterns = listOf(
                Regex("#include\\s*<\\w+(\\.\\w+)?>", RegexOption.IGNORE_CASE),
                Regex("int\\s+main\\s*\\(\\s*(void|int|char)", RegexOption.IGNORE_CASE),
                Regex("(printf|scanf|fprintf|sprintf)\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("(malloc|calloc|realloc|free)\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("size_t\\s+", RegexOption.IGNORE_CASE),
                Regex("typedef\\s+struct\\s+", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("auto", "break", "case", "char", "const", "continue", "default", "do", "double", "else", "enum", "extern", "float", "for", "goto", "if", "int", "long", "register", "return", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while", "NULL", "FILE"),
            antiPatterns = listOf("public class", "class ", "std::", "cout", "template<", "import java", "SELECT FROM"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "HTML",
            uniqueMarkers = listOf("<!DOCTYPE html>", "<html", "</html>", "<head>", "<body>"),
            strongPatterns = listOf(
                Regex("<(!DOCTYPE|html|head|body|div|span|p|a|img|ul|ol|li|table|tr|td|th|form|input|button|select|option|textarea|header|nav|main|section|article|footer|aside)", RegexOption.IGNORE_CASE),
                Regex("</\\w+>", RegexOption.IGNORE_CASE),
                Regex("<\\w+\\s+[^>]*(class|id|style|src|href)=\"[^\"]*\"", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("class=", "id=", "style=", "src=", "href=", "type=", "name=", "value=", "placeholder=", "onclick=", "onsubmit=", "data-", "aria-"),
            antiPatterns = listOf("public class", "def ", "func ", "SELECT FROM"),
            minConfidence = 30
        ),
        LanguageRule(
            language = "CSS",
            uniqueMarkers = emptyList(),
            strongPatterns = listOf(
                Regex("@media\\s+", RegexOption.IGNORE_CASE),
                Regex("@keyframes\\s+", RegexOption.IGNORE_CASE),
                Regex("@import\\s+url", RegexOption.IGNORE_CASE),
                Regex("(margin|padding|border|color|background|font|display|position|width|height|z-index|overflow|opacity|transform|transition|animation|flex|grid|align|justify)\\s*:\\s*[^;]+;", RegexOption.IGNORE_CASE),
                Regex("\\.\\w+\\s*\\{|#\\w+\\s*\\{", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("margin", "padding", "border", "color", "background", "font-size", "font-weight", "font-family", "display", "position", "width", "height", "z-index", "overflow", "opacity", "transform", "transition", "animation", "flex", "grid", "align-items", "justify-content", "flex-direction", "gap", "box-shadow", "text-align", "line-height", "border-radius", "max-width", "min-height"),
            antiPatterns = listOf("public class", "def ", "func ", "SELECT FROM", "int main"),
            minConfidence = 30
        ),
        LanguageRule(
            language = "Dockerfile",
            uniqueMarkers = listOf("FROM ", "WORKDIR ", "EXPOSE ", "RUN ", "CMD ", "ENTRYPOINT ", "COPY ", "ADD ", "ENV ", "ARG "),
            strongPatterns = listOf(
                Regex("^FROM\\s+\\S+", RegexOption.IGNORE_CASE),
                Regex("^WORKDIR\\s+", RegexOption.IGNORE_CASE),
                Regex("^EXPOSE\\s+\\d+", RegexOption.IGNORE_CASE),
                Regex("^RUN\\s+", RegexOption.IGNORE_CASE),
                Regex("^CMD\\s+", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("FROM", "WORKDIR", "EXPOSE", "RUN", "CMD", "ENTRYPOINT", "COPY", "ADD", "ENV", "ARG", "LABEL", "VOLUME", "USER", "HEALTHCHECK", "SHELL", "STOPSIGNAL", "ONBUILD", ".dockerignore"),
            antiPatterns = listOf("public class", "def ", "import ", "SELECT FROM"),
            minConfidence = 20
        ),
        LanguageRule(
            language = "Dart",
            uniqueMarkers = listOf("import 'package:", "void main(", "BuildContext", "Widget build"),
            strongPatterns = listOf(
                Regex("import\\s+'package:", RegexOption.IGNORE_CASE),
                Regex("class\\s+\\w+\\s+extends\\s+(StatefulWidget|StatelessWidget|State)", RegexOption.IGNORE_CASE),
                Regex("Widget\\s+build\\s*\\(BuildContext", RegexOption.IGNORE_CASE),
                Regex("final\\s+\\w+\\s*=\\s*", RegexOption.IGNORE_CASE),
                Regex("const\\s+\\w+\\s*\\(", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("import", "export", "class", "extends", "implements", "with", "mixin", "abstract", "final", "const", "var", "void", "String", "int", "double", "bool", "List", "Map", "Set", "Future", "Stream", "async", "await", "yield", "return", "if", "else", "for", "while", "do", "switch", "case", "break", "continue", "try", "catch", "finally", "throw", "rethrow", "new", "this", "super", "required", "late"),
            antiPatterns = listOf("#include", "public static void main", "SELECT FROM"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "Assembly",
            uniqueMarkers = listOf("section .", "global _start", "mov ", "add ", "sub ", "push ", "pop ", "call ", "ret"),
            strongPatterns = listOf(
                Regex("section\\s+\\.(text|data|bss|rodata)", RegexOption.IGNORE_CASE),
                Regex("global\\s+_start", RegexOption.IGNORE_CASE),
                Regex("\\b(mov|add|sub|push|pop|call|ret|jmp|cmp|int|xor|and|or|shl|shr|inc|dec|lea|nop|lodsb|stosb|rep)\\s+", RegexOption.IGNORE_CASE),
                Regex("\\b(eax|ebx|ecx|edx|esi|edi|ebp|esp|rax|rbx|rcx|rdx|rsi|rdi|rbp|rsp|r8|r9|r10|r11|r12|r13|r14|r15)\\b", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("mov", "add", "sub", "push", "pop", "call", "ret", "jmp", "cmp", "int", "xor", "and", "or", "shl", "shr", "inc", "dec", "lea", "nop", "eax", "ebx", "ecx", "edx", "esi", "edi", "ebp", "esp", "rax", "rbx", "rcx", "rdx", "r8-r15", "syscall", "int 0x80", "db", "dw", "dd", "dq", "resb", "resw", "resd", "resq", "times", "equ"),
            antiPatterns = listOf("public class", "def ", "console.log", "SELECT FROM", "import java"),
            minConfidence = 30
        ),
        LanguageRule(
            language = "MATLAB",
            uniqueMarkers = listOf("function [", "endfunction", "clc", "clear all"),
            strongPatterns = listOf(
                Regex("function\\s+\\[", RegexOption.IGNORE_CASE),
                Regex("endfunction", RegexOption.IGNORE_CASE),
                Regex("(plot|scatter|bar|hist|surf|mesh|contour|imagesc|imshow)\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("(zeros|ones|eye|rand|randn|linspace|logspace)\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("xlabel\\(|ylabel\\(|title\\(|legend\\(|grid\\s+(on|off)", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("function", "end", "if", "else", "elseif", "for", "while", "switch", "case", "otherwise", "try", "catch", "return", "break", "continue", "global", "persistent", "clear", "clc", "close", "figure", "subplot", "hold", "axis", "xlabel", "ylabel", "title", "legend", "grid"),
            antiPatterns = listOf("public class", "def ", "func ", "SELECT FROM", "#include"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "R",
            uniqueMarkers = listOf("<-", "%>%", "library(", "ggplot("),
            strongPatterns = listOf(
                Regex("\\w+\\s*<-\\s*", RegexOption.IGNORE_CASE),
                Regex("%>%\\s*$", RegexOption.MULTILINE),
                Regex("library\\s*\\(\\w+\\)", RegexOption.IGNORE_CASE),
                Regex("ggplot\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("data\\.frame\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("(read|write)\\.(csv|table|xlxs|rds|RData)\\s*\\(", RegexOption.IGNORE_CASE),
                Regex("(lm|glm|anova|t\\.test|chisq\\.test)\\s*\\(", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("library", "require", "source", "if", "else", "for", "while", "repeat", "break", "next", "function", "return", "NA", "NULL", "NaN", "TRUE", "FALSE", "Inf", "c(", "list(", "matrix(", "array(", "data.frame(", "tibble(", "as.", "is.", "str(", "summary(", "head(", "tail(", "names(", "rownames(", "colnames(", "dim(", "length(", "nrow(", "ncol(", "print(", "cat(", "paste(", "sprintf("),
            antiPatterns = listOf("public class", "def ", "func ", "SELECT FROM", "#include"),
            minConfidence = 40
        ),
        LanguageRule(
            language = "JSON",
            uniqueMarkers = emptyList(),
            strongPatterns = listOf(
                Regex("^\\s*\\{\\s*\"\\w+\"\\s*:", RegexOption.MULTILINE),
                Regex("\"\\w+\"\\s*:\\s*\\[", RegexOption.IGNORE_CASE),
                Regex("^\\s*\\[\\s*\\{", RegexOption.MULTILINE)
            ),
            keywords = emptyList(),
            antiPatterns = listOf("public class", "def ", "func ", "SELECT FROM", "import ", "function"),
            minConfidence = 20
        ),
        LanguageRule(
            language = "YAML",
            uniqueMarkers = emptyList(),
            strongPatterns = listOf(
                Regex("^\\s*\\w+:\\s*$", RegexOption.MULTILINE),
                Regex("^\\s*-\\s+\\w+:", RegexOption.MULTILINE),
                Regex("^---\\s*$", RegexOption.MULTILINE),
                Regex("\\bare:.*name:\\s*\\w+", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("apiVersion:", "kind:", "metadata:", "spec:", "selector:", "template:", "containers:", "image:", "ports:", "env:", "resources:", "volumes:", "services:", "deployment:", "configmap:", "secret:", "namespace:", "ingress:", "pipeline:"),
            antiPatterns = listOf("public class", "def ", "func ", "SELECT FROM", "<!DOCTYPE"),
            minConfidence = 20
        ),
        LanguageRule(
            language = "XML",
            uniqueMarkers = listOf("<?xml", "xmlns:", "<?xml-stylesheet"),
            strongPatterns = listOf(
                Regex("<\\?xml\\s+", RegexOption.IGNORE_CASE),
                Regex("xmlns:\\w+\\s*=\\s*\"", RegexOption.IGNORE_CASE),
                Regex("<\\w+\\s+android:", RegexOption.IGNORE_CASE),
                Regex("</\\w+>", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("<?xml", "<resources>", "<manifest>", "<application>", "<activity>", "<intent-filter>", "<action>", "<category>", "<data>", "<meta-data>", "<uses-permission>", "<uses-feature>", "<provider>", "<receiver>", "<service>"),
            antiPatterns = listOf("public class", "def ", "func ", "SELECT FROM", "import java"),
            minConfidence = 30
        ),
        LanguageRule(
            language = "Markdown",
            uniqueMarkers = emptyList(),
            strongPatterns = listOf(
                Regex("^#{1,6}\\s+\\w+", RegexOption.MULTILINE),
                Regex("\\[.+\\]\\(.+\\)", RegexOption.IGNORE_CASE),
                Regex("^[-*+]\\s+\\w+", RegexOption.MULTILINE),
                Regex("^\\d+\\.\\s+\\w+", RegexOption.MULTILINE),
                Regex("\\*\\*\\w+\\*\\*|__\\w+__"),
                Regex("^>\\s+", RegexOption.MULTILINE),
                Regex("```\\w*\\n", RegexOption.IGNORE_CASE)
            ),
            keywords = listOf("# ", "## ", "### ", "#### ", "##### ", "###### ", "> ", "```", "---", "***", "___", "| ", "| ---"),
            antiPatterns = listOf("public class", "SELECT ", "def ", "func "),
            minConfidence = 20
        )
    )
}
