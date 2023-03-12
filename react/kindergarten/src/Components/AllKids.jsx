import {useEffect, useState} from "react";
import KidsService from "../API/KidsService";
import {Button, Modal} from "react-bootstrap";
import ChildForm from "./ChildForm";
import KidBrothersAndSisters from "./KidBrothersAndSisters";
import Relatives from "./Relatives";

function AllKids({tokens, setTokens}) {
    const [kids, setKids] = useState([]);
    const [addChildForm, setAddChildForm] = useState(false);
    const [updateChildForm, setUpdateChildForm] = useState(false);
    const [brothersAndSisters, setBrothersAndSisters] = useState(false);
    const [selectedKid, setSelectedKid] = useState(null);
    const [relatives, setRelatives] = useState(false);
    useEffect(() => {
        async function fetchData() {
            const data = await KidsService.getAll(tokens, setTokens);
            setKids(data);
        }

        fetchData();
    }, []);

    const handleDelete = (id) => {
        KidsService.delete(id, tokens, setTokens).then((data) => {
            console.log(data);
            if (!data) {
                setKids([...kids.filter((k) => k.id !== id)]);
            } else alert(data.debugMessage);
        });
    };

    const handleShowBrothersAndSisters = (kid) => {
        setSelectedKid(kid);
        setBrothersAndSisters(true);
    };

    function handleUpdate(kid) {
        setSelectedKid(kid);
        setUpdateChildForm(true);
    }

    function handleRelative(kid) {
        setSelectedKid(kid)
        setRelatives(true)
    }

    return (
        <div>
            {kids.length === 0 ? (
                <p>Kids not found</p>
            ) : (
                <div className="kids-list-container">
                    <h3>All kids</h3>
                    <ul className="kids-list">
                        {kids.map((kid) => (
                            <div>
                                <div>
                                    <li className="kids-list-item" key={kid.id}>
                                        <span className="kid-name">{kid.name}</span>
                                        <span className="birth-year">({kid.birthYear})</span>
                                        <div className="button-container">
                                            <Button variant="primary" onClick={() => handleUpdate(kid)}>update</Button>
                                            <Button variant="danger"
                                                    onClick={() => handleDelete(kid.id)}>delete</Button>
                                            <Button variant="primary" onClick={() => handleShowBrothersAndSisters(kid)}>brothers
                                                and sisters</Button>
                                            {
                                                relatives ?
                                                    <div>
                                                        <Button variant="dark"
                                                                onClick={() => handleRelative(kid)} > close < /Button>

                                                    </div>
                                                    :
                                                    <Button variant="primary"
                                                            onClick={() => setRelatives(true)}> relatives</Button>
                                            }
                                        </div>
                                    </li>
                                </div>
                                <div>
                                    {
                                        relatives ? <Relatives tokens={tokens} setTokens={setTokens} kidId={selectedKid?.id}/> : <br/>

                                    }
                                </div>
                            </div>
                        ))}
                    </ul>
                </div>
            )}
            <div>
                <Button variant="primary" className="m-3" onClick={() => setAddChildForm(true)}>Add
                    Child</Button>
                <Modal show={addChildForm} onHide={setAddChildForm}>
                    <ChildForm kids={kids} setKids={setKids} tokens={tokens} setTokens={setTokens}
                               setShowForm={setAddChildForm}/>
                </Modal>
            </div>
            <Modal show={brothersAndSisters} onHide={setBrothersAndSisters}>
                <KidBrothersAndSisters id={selectedKid?.id} tokens={tokens} setTokens={setTokens}/>
            </Modal>
            <Modal show={updateChildForm} onHide={setUpdateChildForm}>
                <ChildForm kids={kids} setKids={setKids} tokens={tokens} setTokens={setTokens}
                           setShowForm={setUpdateChildForm} child={selectedKid}/>
            </Modal>
        </div>
    );
}

export default AllKids;