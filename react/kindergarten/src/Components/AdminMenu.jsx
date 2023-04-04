import {useState} from "react";
import AllChild from "./AllChild";
import AllRelatives from "./AllRelatives";
import {Button} from "react-bootstrap";

function AdminMenu({tokens, setTokens}) {
    const [selectedOption, setSelectedOption] = useState('default');

    const handleOptionSelect = (option) => {
        setSelectedOption(option);
    }

    const content =
        selectedOption === 'kids' ? (
            <AllChild tokens={tokens} setTokens={setTokens}/>
        ) : selectedOption === 'relatives' ? (
            <AllRelatives tokens={tokens} setTokens={setTokens}/>
        ) : (
            <h3>Please select an option</h3>
        );
    return (
        <div>
            <div className="element-action">
                <Button variant="primary" onClick={() => handleOptionSelect('kids')}>Get all kids</Button>
                <Button variant="secondary" onClick={() => handleOptionSelect('relatives')}>Get all relatives</Button>
            </div>
            {content}
        </div>
    );
}

export default AdminMenu;