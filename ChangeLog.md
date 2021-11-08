2021-11 - 1.0.0-alpha

- remove --j7 option
- cleanup, some of which related with duplicate field names in input schema:
  such duplications cannot be detected due to how Typesafe Config works;
  for example, the following is OK from Config perspective, but only
  one of the `foo` definitions will be seen by tscfg

        myStruct: {
          foo: string
          baz: string
          foo: int
        }

- starting v1
- [previous change log](v0/ChangeLog0).
