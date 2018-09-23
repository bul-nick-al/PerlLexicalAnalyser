# HA-2: Lexical analyzer for the Perl 6 Specifications

### by Rozaliya Amirova and Nikolay Buldakov (team 31)

#### Description.


This document provides a general description of the tokens the lexer may use. Since it was not specified in the task, for which version of Perl we have to implement the lexer, we have chosen Perl6. And since Perl6 is in development and its structure and format are constantly under discussion, it is possible that tokens may vary significantly. The source we have chosen for the reference can be found [here](https://perl6intro.com)

#### Tokens. 

There are in total __104__ tokens the lexer can recognise. They are conceptually divided into subclasses:
- Operators
- Keywords
- Paired tokens
- Ambiguous tokens
- Lexical units
- Dot, comma, colon, semicolon
- Auxiliary tokens

They will be described below. Each token consists of two things:
1. __Name__ of the token 
2. __Literal__ corresponding to the token

##### Operators. 
Operators of Perl6, such as `+`, `-`, `/`, etc. have names, that end with `_OPERATORS`. For example, the plus sign, `+`, will be tokenized as this: 
```
ADDITION_OPERATOR, +
```
The full list of operators and the corresponding literals can be found either [on the reference site](https://perl6intro.com) or in the `Token.java` file.

##### Keywords. 
Keywords of Perl6, such as `my`, `for`, `does`, etc. are the reserved words of the language. The corresponding token names end with `_KEYWORD`. For example, the keyword `method` will be tokenized as this: 
```
METHOD_KEYWORD method
```
The full list of keywords and the corresponding literals can be found either [on the reference site](https://perl6intro.com) or in the `Token.java` file.

##### Paired tokens. 
Paired tokens of Perl6, are the once that have the corresponing opposite token. These are `{` and `}`, `<` and `>`, `[` and `]`, `(` and `)`. The corresponding token names starts with either `LEFT_` or `RIGHT_`. For example, the curly bracket `{` will be tokenized as this: 
```
LEFT_CURLY_BRACKET {
```

##### Ambiguous tokens. 
Some literals in Perl can mean differnt things depending on the context. For example, `%` is used both for hashes (e.g. `%my-hash`) and for modulo. In such cases just the name of the token is just the same as the literal and it is up to the following stages to identify its actual meaning. For example, `?` will be tokenized as this: 
```
QUESTION_MARK ?
```
The full list of ambiguous tokens can be found in the `Token.java` file.

##### Lexical units. 
Lexical units in Perl6 are `identifiers`, `numbers`, `boolens` and `regexes`. The corresponding token names are `IDENTIFIER`, `STRING`, `NUMBER`, `BOOLEAN`, `REGEX`. For example, the regex `s/\s+$//` will be tokenized as this:
```
REGEX s/\s+$//
```
__Note!__ The regex support in Perl6 is tremendously various and complex, can be multi layered and even iclude Perl6 code. Since regex inner structure is not a part of language definition itself, our lexer only __identifies__ regexes but __does not tokenise__ them.

##### Dot, comma, colon, semicolon. 
Tokens' names for dot, comma, colon, semicolon in Perl6 are `DOT`, `COMMA`, `COLON`, `SEMICOLON`. For example, `;` will be tokenized as this:
```
SEMICOLON ;
```

##### Auxiliary tokens. 
These are the tokens which are used by the lexes to report service information and they do not represent anything from the Perl6 defnition. There are only two such tokens: `END_OF_INPUT` and `ERROR`. For example, when there are no more literal to tokenize, i.e. all code has been parses, the following token is returned:
```
END_OF_INPUT 
```

#### P.S.
In this document we only provide a general description of the tokens of the lexer. The detailed information about them can be inferred from this document used along with the `Token.java` file and from [the reference site](https://perl6intro.com)






