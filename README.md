[![](https://travis-ci.org/welovecoding/editorconfig-netbeans.svg?branch=master)](https://travis-ci.org/welovecoding/editorconfig-netbeans)

# EditorConfig NetBeans Plugin


A NetBeans IDE plugin supporting the [EditorConfig][] standard.


## EditorConfig Project

EditorConfig makes it easy to maintain the correct coding style when switching
between different text editors and between different projects.  The
EditorConfig project maintains a file format and plugins for various text
editors which allow this file format to be read and used by those editors.  For
information on the file format and supported text editors, see the
[EditorConfig website][EditorConfig].

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

## Supported properties

1. :construction: **charset** (`latin1`, `utf-8`, `utf-8-bom`, `utf-16be`, `utf-16le`, `utf-8-bom`)
1. :construction: **end_of_line** (`lf`, `cr`, `crlf`)
1. :construction: **indent_size** (`[number]`)
1. :construction: **indent_style** (`space`, `tab`)
1. :white_check_mark: **insert_final_newline** (`false`, `true`)
1. :construction: **tab_width** (`[number]`)
1. :white_check_mark: **trim_trailing_whitespace** (`false`, `true`)

[EditorConfig]: http://editorconfig.org

## Info

- A project must be closed and opened after the plugin is installed, to setup hooks for `.editorconfig` files
- Rules are applied when a file (which is matched by a rule) is saved

## Disclaimer

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN
NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
OR OTHER DEALINGS IN THE SOFTWARE.

## Contributors

[![Benny Neugebauer on Stack Exchange][stack_exchange_flair_bennyn]][stack_exchange_link_bennyn]

[![Michael Koppen on Stack Exchange][stack_exchange_flair_yser]][stack_exchange_link_yser]

## Credits

- [Geertjan Wielenga](https://blogs.oracle.com/geertjan) for his posts on [EditorConfig and NetBeans IDE](https://blogs.oracle.com/geertjan/entry/editorconfig_and_netbeans_ide) [(Part 2)](https://blogs.oracle.com/geertjan/entry/editorconfig_and_netbeans_ide_part) [(Part 3)](https://blogs.oracle.com/geertjan/entry/editorconfig_and_netbeans_ide_part1)

# Inspiring projects

- [Change line endings Plugin](https://github.com/junichi11/netbeans-change-lf) by @junichi11
- [Encoding Plugin](https://github.com/junichi11/netbeans-encoding-plugin) by @junichi11
- [JSBeautify](https://github.com/drewhamlett/netbeans-jsbeautify) by @drewhamlett


[stack_exchange_link_bennyn]: http://stackexchange.com/users/203782/benny-neugebauer?tab=accounts
[stack_exchange_link_yser]: http://stackexchange.com/users/3210455/yser?tab=accounts
[stack_exchange_flair_bennyn]: http://stackexchange.com/users/flair/203782.png?theme=default
[stack_exchange_flair_yser]: http://stackexchange.com/users/flair/3210455.png?theme=default
