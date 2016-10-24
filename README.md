# EditorConfig NetBeans Plugin

A NetBeans IDE plugin supporting the [EditorConfig][] standard.

- Supports NetBeans 8 and above
- Requires Java 8

[![](https://travis-ci.org/welovecoding/editorconfig-netbeans.svg?branch=master)](https://travis-ci.org/welovecoding/editorconfig-netbeans)

[**DOWNLOAD**](https://github.com/welovecoding/editorconfig-netbeans/releases)

## Features

![Plugin Screenshot](https://dl.dropboxusercontent.com/u/74217418/screenshots/github/editorconfig-plugin/screen-1.png)

- Reads EditorConfig files
- [Syntax highlighting](https://dl.dropboxusercontent.com/u/74217418/screenshots/github/editorconfig-plugin/syntax-highlighting.png)
- [Navigation in EditorConfig files](https://dl.dropboxusercontent.com/u/74217418/screenshots/github/editorconfig-plugin/navigator-with-go-to-source.png)

## EditorConfig Project

EditorConfig makes it easy to maintain the correct coding style when switching between different text editors and between different projects.  The EditorConfig project maintains a file format and plugins for various text editors which allow this file format to be read and used by those editors. For information on the file format and supported text editors, see the [EditorConfig website][EditorConfig].

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

###  :construction:  1. charset

*Values:* `latin1`, `utf-8`, `utf-8-bom`, `utf-16be`, `utf-16le`

### :white_check_mark: 2. end_of_line

*Values:* `lf`, `cr`, `crlf`

### :white_check_mark: 3. indent_size

*Values:* `[number]`, `tab`

*Special case:*
Indent_size can be set to `tab` if `indent_size` is unspecified and `indent_style` is set to `tab`.
When set to `tab`, the value of `tab_width` (if specified) will be used.

Read our notes on [Indentation](https://github.com/welovecoding/editorconfig-netbeans/wiki/EditorConfig---Rule-Evaluation#indentation).

### :white_check_mark: 4. indent_style

*Values:* `space`, `tab`

Read our notes on [Indentation](https://github.com/welovecoding/editorconfig-netbeans/wiki/EditorConfig---Rule-Evaluation#indentation).

### :white_check_mark: 5. insert_final_newline

*Values:* `false`, `true`

### :white_check_mark: 6. tab_width

*Values:* `[number]`

*Special case:*
Defaults to the value of `indent_size` and doesn't usually need to be specified.

Read our notes on [Indentation](https://github.com/welovecoding/editorconfig-netbeans/wiki/EditorConfig---Rule-Evaluation#indentation).

### :white_check_mark: 7. trim_trailing_whitespace

*Values:* `false`, `true`

[EditorConfig]: http://editorconfig.org

## Info

- A project must be closed and opened after the plugin is installed, to setup hooks for `.editorconfig` files
- Rules are applied when a file (which is matched by a rule) is saved

## Disclaimer

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

## Contributors

[![Benny Neugebauer](https://avatars1.githubusercontent.com/u/469989?v=3&s=100)](http://www.bennyn.de/) | [![Michael Koppen](https://avatars1.githubusercontent.com/u/1138344?v=3&s=100)](http://beanbelt.blogspot.de/) | [![Junichi Yamamoto](https://avatars1.githubusercontent.com/u/738383?v=3&s=100)](http://junichi11.com/)
:---:|:---:|:---:
[**Benny Neugebauer**](http://www.bennyn.de/) | [**Michael Koppen**](http://beanbelt.blogspot.de/) | [**Junichi Yamamoto**](http://junichi11.com/)

### Special Credits
- [Geertjan Wielenga](https://blogs.oracle.com/geertjan) for his posts on [EditorConfig and NetBeans IDE](https://blogs.oracle.com/geertjan/entry/editorconfig_and_netbeans_ide)
