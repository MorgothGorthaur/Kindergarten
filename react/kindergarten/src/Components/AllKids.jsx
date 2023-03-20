import {useEffect, useState} from "react";
import KidsService from "../API/KidsService";
import {Button, Modal} from "react-bootstrap";
import ChildForm from "./ChildForm";
import KidBrothersAndSisters from "./KidBrothersAndSisters";
import Relatives from "./Relatives";
import KidListItem from "./KidListItem";
import Loader from "../UI/Loader/Loader";
import React from "react";

function AllKids({tokens, setTokens, group, setGroup}) {
    const [kids, setKids] = useState([]);
    const [addChildForm, setAddChildForm] = useState(false);
    const [loader, setLoader] = useState(true);
    useEffect(() => {
        if(loader) {
            async function fetchData() {
                const data = await KidsService.getAll(tokens, setTokens);
                setKids(data);
                setLoader(false);
            }

            fetchData();
        }
    }, [loader]);


    return (
        <div>
            {loader ? (
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100vh' }}>
                    <Loader />
                </div>
            ) : (
                <div>
                    {kids && kids.length === 0 ? (
                        <p>Kids not found</p>
                    ) : (
                        <div className="kids-list-container">
                            <h3>All kids</h3>
                            <ul className="kids-list">
                                {kids.map((kid) => (
                                    <KidListItem kid={kid} tokens={tokens} setTokens={setTokens} kids={kids}
                                                 setKids={setKids} group={group} setGroup={setGroup}/>
                                ))}
                            </ul>
                        </div>
                    )}
                    <div>
                        <Button variant="primary" className="m-3" onClick={() => setAddChildForm(true)}>Add
                            Child</Button>
                        <Modal show={addChildForm} onHide={setAddChildForm}>
                            <ChildForm kids={kids} setKids={setKids} tokens={tokens} setTokens={setTokens}
                                       setShowForm={setAddChildForm} group={group} setGroup={setGroup}/>
                        </Modal>
                    </div>
                </div>
            )}
        </div>
    );
}

export default AllKids;