import {useEffect, useState} from "react";
import KidsService from "../API/KidsService";
import {Button, Modal} from "react-bootstrap";
import ChildForm from "./ChildForm";

function AllKids({tokens, setTokens}) {
    const [kids, setKids] = useState([]);
    const [addChildForm, setAddChildForm] = useState(false);
    const [updateChildForm, setUpdateChildForm] = useState(false);
    useEffect(() => {
        async function fetchData() {
            const data = await KidsService.getAll(tokens, setTokens);
            setKids(data);
        }

        fetchData();
    }, []);
    const handleDelete = (id) => {
        KidsService.delete(id, tokens, setTokens).then(data => {
            console.log(data);
            if (!data) {
                setKids([...kids.filter(k => k.id !== id)])
            } else alert(data.debugMessage);
        });
    };
    return (
        <div>
            {kids.length === 0 ? (
                <p>Kids not found</p>
            ) : (
                <div className="kids-list-container">
                    <h3>All kids</h3>
                    <ul className="kids-list">
                        {kids.map((kid) => (
                            <li className="kids-list-item" key={kid.id}>
                                <span className="kid-name">{kid.name}</span>
                                <span className="birth-year">({kid.birthYear})</span>
                                <Button variant="primary" onClick={() => setUpdateChildForm(true)}>update</Button>
                                <Button variant="danger" onClick={() => handleDelete(kid.id)}>delete</Button>
                                <Modal show={updateChildForm} onHide={setUpdateChildForm}> <ChildForm kids={kids} setKids={setKids} tokens={tokens} setTokens={setTokens} setShowForm={setUpdateChildForm} child={kid}/></Modal>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
            <div>
                <Button variant="primary" className="m-3" onClick={() => setAddChildForm(true)}>Add Child</Button>
                <Modal show={addChildForm} onHide={setAddChildForm}> <ChildForm kids={kids} setKids={setKids} tokens={tokens} setTokens={setTokens} setShowForm={setAddChildForm}/></Modal>
            </div>
        </div>
    );
}

export default AllKids;