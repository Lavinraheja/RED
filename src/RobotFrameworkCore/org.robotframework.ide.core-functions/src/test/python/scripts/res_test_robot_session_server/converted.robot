*** Test Cases ***
test
    Log    1
    kw    \    \    c

*** Keywords ***
kw
    [Arguments]    ${x}    ${y}    ${z}
    Log    ${x}, ${y}, ${z}
