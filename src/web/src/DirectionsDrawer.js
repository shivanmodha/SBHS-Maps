import React, { Component } from 'react';
import { InputGroupButton, InputGroupAddon, InputGroup, Input, Button } from 'reactstrap';
import { Icon } from 'react-fa'

class DirectionsDrawer extends Component
{
    render()
    {
        return (
            <div style={{
                backgroundColor: "white",
                marginTop: 5,
                borderBottomLeftRadius: 10,
                borderBottomRightRadius: 10
            }}>
                <InputGroup style={{ border: "none" }}>
                    <InputGroupAddon style={{ border: "none", paddingLeft: 0, paddingRight: 0, width: 50, paddingTop: 12, paddingBottom: 12, borderBottomLeftRadius: 10, borderTopLeftRadius: 0 }}>
                        End
                    </InputGroupAddon>
                    <Input style={{ border: "none" }} placeholder="Choose Destination..." />
                    <InputGroupButton>
                        <Button outline color="primary" style={{ padding: 0, width: 50, border: "none" }}>
                            <Icon name="search" />
                        </Button>
                    </InputGroupButton>
                    <InputGroupAddon style={{ padding: 0, width: 2, marginTop: 10, marginBottom: 10 }}></InputGroupAddon>
                    <InputGroupButton>
                        <Button outline color="warning" style={{ padding: 0, width: 50, border: "none", borderBottomRightRadius: 10, borderTopRightRadius: 0 }}>
                            <Icon name="angle-right" />
                        </Button>
                    </InputGroupButton>
                </InputGroup>
            </div>
        );
    }
}
export default DirectionsDrawer;
