import {useState} from 'react';
import KidsThatWaitForBirthday from './KidsThatWaitForBirthday';
import AllKids from './AllKids';
import KidsWithRelatives from './KidsWithRelatives';
import {Button} from 'react-bootstrap';

function KidsMenu({tokens, setTokens, group, setGroup}) {
    const [selectedOption, setSelectedOption] = useState('default');

    const handleOptionSelect = (option) => {
        setSelectedOption(option);
    }

    const content =
        selectedOption === 'kids-with-relatives' ? (
            <KidsWithRelatives tokens={tokens} setTokens={setTokens}/>
        ) : selectedOption === 'all-kids' ? (
            <AllKids tokens={tokens} setTokens={setTokens} group={group} setGroup={setGroup}/>
        ) : selectedOption === 'kids-wait-birthday' ? (
            <KidsThatWaitForBirthday tokens={tokens} setTokens={setTokens}/>
        ) : (
            <h3>Please select an option</h3>
        );
    return (
        <div>
            <div className="element-action">
                <Button variant="primary" onClick={() => handleOptionSelect('kids-with-relatives')}>Get kids with
                    relatives</Button>
                <Button variant="secondary" onClick={() => handleOptionSelect('all-kids')}>Get all kids</Button>
                <Button variant="success" onClick={() => handleOptionSelect('kids-wait-birthday')}>Get kids that wait
                    for birthday</Button>
            </div>
            {content}
        </div>
    );
}

export default KidsMenu;
