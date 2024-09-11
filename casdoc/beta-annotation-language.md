## Code Annotation Language

### Casdoc Comment

Each annotation is declared as a special block comment that must begin with `/*?`. This special block comment is called here a Casdoc comment.

The first and last lines of the comment, which contain the opening (`/*?`) and closing sequences (`*/`), are not parsed. The Casdoc comment should not appear on the same line as other parts of the code, including another Casdoc comment. Thus, the opening and closing sequences should appear alone on their line, except for whitespace.

For example:
```java
/*?
 * Good Casdoc comment
 */

/*? this part is ignored
 * 
 * Casdoc comment
 * 
 * this part is also ignored */

int someCode; /*?
               * Bad Casdoc comment
			   * opening sequence is on the same line as some code
			   */

/*?
 * also bad
 */ /*?
 * don't place 2 Casdoc comments next to each other
 */

/*
 * Regular block comments
 * will not be parsed or removed by Casdoc.
 */
```

Each annotation must be declared in a Casdoc comment placed immediately before the line where the anchor starts (either the line where the inline anchor is or the first line of a block anchor). If multiple annotations have anchors that start on the same line, they must all be placed in the same, single Casdoc comment. Within a Casdoc comment, each annotation must be separated by a line containing only 3 or more plus signs (`+++`), surrounded by a blank line before and after.

#### Indentation

The indentation of the comment must be consistent. If all lines start with some indentation sequence, this indentation will be removed. The first asterisk on each line is also considered part of the indentation (and removed) if it appears on every line at the same place. Blank lines do not affect the computation of the indentation. An inconsistent use of space and tab characters may cause the computation of the indentation to fail.

### Annotation Structure

All annotations follow a similar structure: preamble lines, followed by a line containing 3 or more dashes without surrounding whitespace (`---`), followed by the content of the annotation in markdown format. An annotation with no content will be ignored with an error.

#### Preamble

Annotations start with a preamble, which consists of a list of key-value pairs (some required and some optional, depending on the annotation type). The order of the preamble lines does not matter, and the keys are case-insensitive.

The preamble of each annotation must contain the declaration of the annotation type, followed by the anchor identifier, for example `Type:Keyword`. `Type` must not be preceded by whitespace (excluding indentation), but the value will be stripped of leading and trailing whitespace (thus, an anchor can't contain only whitespace).

**For all annotations:**
* `Type` **(required)**: The type of anchor and annotation.
- `Keyword` for inline anchors;
- `Block` for block anchors;
- `Internal` for anchors nested in another annotation.
* `ID` (optional): An ID for the annotation, unique within the Casdoc comment; the value must be non empty and contain only alphanumeric characters, hyphens, and underscores. If the field is not present, a default value is given (based on the anchor or title, depending on the type).
* `URL` (optional, repeatable): Allows to add "see also" links; the value must start with a valid URL, and optionally contain the reference label after the URL and a space. These links will appear in the long form of the annotation as buttons, but not in the short form. If there is no text, a default label is used. For example: 

```
URL: https://www.example.com
URL: https://www.example.com this is my custom label
```

* `Offset` (optional): The number of lines that the Casdoc comment is offset relative to the line where the anchor actually is; the value must be a number (positive or negative).
* `Sequence` (optional): The index at which to include this annotation in the sequence, which serves as a 'walkthrough' composed of related fragments. For example: `Sequence: 1`. The index must be an integer, but has no further restrictions - the annotations will be placed in the order of the given indices. 

**Type-Specific**
* `Anchor` (required for keyword and internal annotations): The exact text of the code that is annotation will be anchored in.    
* `Range` (required for block): The number of lines the block spans. The number of lines to indicate for block annotations must consider every line in the file, including blank lines and lines taken by other Casdoc comments. Casdoc comments will be removed from the main code example of the final document, and the number of lines of the block anchor will then be adjusted accordingly. The first line (i.e., the line immediately after the Casdoc comment where the annotation is declared, plus offset if applicable) and the last line (first line + range - 1) of the anchor must be in the file (i.e., not part of a Casdoc comment).
* `Title` (required for block): The title to appear above the content of this annotation.
* `Parent` (required for internal): The ID of the parent annotation.


#### Content of the Annotation

After the preamble and separation lines (line of 3+ dashes `---`) have been declared, all other lines become the content of the annotation. Blank lines that appear at the start of the content are removed (so it is possible to add an empty line between the preamble of an annotation and its content). They support markdown formatting, which will be converted to HTML using the [CommonMark specification](https://commonmark.org/).

Note one counter-intuitive feature of the CommonMark specification: if a line starts with what looks like an HTML tag, it may disable parsing the rest of the line and subsequent lines. In general, it is best to avoid starting lines with an angle bracket (`<`), unless you know what you're doing and carefull check the output document. This behavior is described in Section 4.6 of the specification [here](https://spec.commonmark.org/0.30/#html-blocks).

**Nested Anchors and Markdown:** If the anchor text of an internal annotation overlaps with some markdown syntax, the identifier of the nested annotation must contain the same markdown syntax. The anchor of a nested annotation should not split Markdown syntax tokens or HTML tags (which are legal in Markdown).

#### Examples

```java
/*?
 * Type: Keyword 
 * Anchor: main
 * Sequence: 1
 * ---
 * This is the content of the annotation.
 *
 * +++
 * 
 * Type:Internal 
 * Anchor: content
 * Parent: main
 * ---
 * This is a nested annotation, whose anchor is the term
 * "content" in the first annotation.
 *
 * +++
 * 
 * Type: Internal
 * Anchor: is :2
 * Parent: content
 * ---
 * This is another nested annotation, whose anchor is
 * in the previous annotation. It's the first occurrence
 * of the verb "is", which is actually the second
 * occurrence of the substring "is" (the first is the
 * last 2 letters of "This").
 * 
 * +++
 *
 * Type:Block
 * Range: 3
 * Title: Title of the annotation
 * Sequence: 2
 * ---
 * This is the content of a block annotation.
 * Remember that [markdown](https://commonmark.org/)
 * is supported in annotations!
 * 
 * +++
 * 
 * Type: Internal
 * Anchor: block annotation
 * Parent: Title of the annotation
 * ---
 * An annotation nested in the block annotation.
 * 
 * Note the `Parent` line of this annotation: it's the title of the block annotation, which is its default ID.
 */
public static void main(String[] args) {
	// ...
}
```
