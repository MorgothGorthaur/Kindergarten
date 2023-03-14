import { useState } from 'react';
import KidsThatWaitForBirthday from './KidsThatWaitForBirthday';
import AllKids from './AllKids';
import KidsWithRelatives from './KidsWithRelatives';
import {Button} from 'react-bootstrap';
function KidsMenu({tokens, setTokens, group, setGroup}) {
    const [selectedOption, setSelectedOption] = useState('default');

    const handleOptionSelect = (option) => {
        setSelectedOption(option);
    }

    switch (selectedOption) {
        case 'kids-with-relatives':
            return <KidsWithRelatives tokens={tokens} setTokens={setTokens} />;
        case 'all-kids':
            return <AllKids tokens={tokens} setTokens={setTokens} group={group} setGroup={setGroup}/>;
        case 'kids-wait-birthday':
            return <KidsThatWaitForBirthday tokens={tokens} setTokens={setTokens} />;
        default:
            return (
                <div className="element-action">
                    <Button variant="primary" onClick={() => handleOptionSelect('kids-with-relatives')}>Get kids with relatives</Button>
                    <Button variant="secondary" onClick={() => handleOptionSelect('all-kids')} >Get all kids</Button>
                    <Button variant="success" onClick={() => handleOptionSelect('kids-wait-birthday')}>Get kids that wait for birthday</Button>
                </div>
            );
    }
}

export default KidsMenu;