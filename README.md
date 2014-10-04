# EditorConfig NetBeans Plugin


A NetBeans IDE plugin supporting the EditorConfig standard. 

An EditorConfig file consists of a file named `.editorconfig` which is usually stored in the root directory of a project. The EditorConfig file defines coding styles across different IDEs (ex. NetBeans IDE, IntelliJ IDEA, Visual Studio, Sublime Text, etc.). 

Read more about EditorConfig at http://editorconfig.org/.

## Example file

**.editorconfig**

```ini
# top-most EditorConfig file
root = true

# Unix-style newlines with a newline ending every file
[*]
end_of_line = lf
insert_final_newline = true

# 4 space indentation
[*.py]
indent_style = space
indent_size = 4

# Tab indentation (no size specified)
[*.js]
indent_style = tab

# Indentation override for all JS under lib directory
[lib/**.js]
indent_style = space
indent_size = 2

# Matches the exact files either package.json or .travis.yml
[{package.json,.travis.yml}]
indent_style = space
indent_size = 2
```
