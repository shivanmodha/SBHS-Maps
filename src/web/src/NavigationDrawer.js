import React, { Component } from 'react';
import { ButtonGroup, Button } from 'reactstrap';

class NavigationDrawer extends Component
{
    render()
    {
        return (
            <div style={{
                backgroundColor: "#f7f7f9",
                padding: 10,
                marginTop: 5,
                borderBottomLeftRadius: 10,
                borderBottomRightRadius: 10
            }}>
                <ButtonGroup>    
                    <Button outline>iOS</Button>
                    <Button outline>Android</Button>
                    <Button outline>Desktop</Button>
                </ButtonGroup>    
            </div>    
        );
    }
}
export default NavigationDrawer;
