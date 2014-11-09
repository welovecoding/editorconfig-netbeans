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

- :white_check_mark: charset
- :white_check_mark: end_of_line
- :white_check_mark: indent_size
- :white_check_mark: indent_style
- :white_check_mark: insert_final_newline
- :white_check_mark: tab_width
- :construction_worker: trim_trailing_whitespace

[EditorConfig]: http://editorconfig.org

## Known issues

- A project must be closed and opened after the plugin is installed, to setup hooks for `.editorconfig` files
- Rules are only applied when a file (which is matched by a rule) is saved
- It takes ~10 seconds to apply the rules after a file has been saved

## Tools

- [Detenc (Character Encoding Detector)](http://www.marblesoftware.com/Marble_Software/Detenc.html)

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

## Thanks to

- [Geertjan Wielenga](https://blogs.oracle.com/geertjan) for his sample project and article on [EditorConfig and NetBeans IDE](https://blogs.oracle.com/geertjan/entry/editorconfig_and_netbeans_ide)


[stack_exchange_link_bennyn]: http://stackexchange.com/users/203782/benny-neugebauer?tab=accounts
[stack_exchange_link_yser]: http://stackexchange.com/users/3210455/yser?tab=accounts
[stack_exchange_flair_bennyn]: http://stackexchange.com/users/flair/203782.png?theme=default
[stack_exchange_flair_yser]: http://stackexchange.com/users/flair/3210455.png?theme=default
